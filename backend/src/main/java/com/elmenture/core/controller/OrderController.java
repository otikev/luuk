package com.elmenture.core.controller;

import com.elmenture.core.model.*;
import com.elmenture.core.payload.DarajaAuthDto;
import com.elmenture.core.payload.StkPushRequestDto;
import com.elmenture.core.payload.StkPushResponseDto;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.repository.OrderItemRepository;
import com.elmenture.core.repository.OrderRepository;
import com.elmenture.core.repository.TransactionDetailsRepository;
import com.google.gson.Gson;
import okhttp3.RequestBody;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.elmenture.core.utils.LuukProperties.*;

/**
 * Created by otikev on 17-Mar-2022
 */
@Transactional
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> fetchOrders() {
        User user = getLoggedInUser();
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PostMapping("/payment-confirmed")
    public void paymentConfirmed(@Valid @org.springframework.web.bind.annotation.RequestBody Object request) {

        String objectJson = new Gson().toJson(request);
        JSONObject body = new JSONObject(objectJson).getJSONObject("Body");
        JSONObject stkCallback = body.getJSONObject("stkCallback");

        Order order = orderRepository.findByMerchantRequestID(stkCallback.getString("MerchantRequestID"));
        if (order == null)
            return;

        if (stkCallback.getInt("ResultCode") == 0) {
            saveTransactionDetails(stkCallback, order);
            executor.execute(() -> sendEmail(order.getId()));
        } else {
            undoCreatedOrder(order);
        }
    }

    private void sendEmail(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<Long> orderItemIds = new ArrayList<>();
        long totalCents = 0;
        String customerName = order.getUser().getFirstName() + order.getUser().getLastName();
        String address = order.getUser().getPhysicalAddress();

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            orderItemIds.add(orderItem.getId());
            totalCents += item.getPrice();
        }

        try {
            emailService.sendNewOrderEmail(order.getId(), orderItemIds, totalCents, customerName, address, "Standard");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void undoCreatedOrder(Order order) {
        orderItemRepository.deleteByOrderId(order.getId());
        orderRepository.delete(order);
    }

    private void saveTransactionDetails(JSONObject stkCallBack, Order order) {
        String merchantId = stkCallBack.getString("MerchantRequestID");
        JSONObject callbackMetadata = stkCallBack.getJSONObject("CallbackMetadata");
        JSONArray itemlist = callbackMetadata.getJSONArray("Item");

        double amount = itemlist.getJSONObject(0).getDouble("Value");
        String mpesaReceiptNumber = itemlist.getJSONObject(1).getString("Value");
        Long phoneNumber = itemlist.getJSONObject(4).getLong("Value");

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        List<Item> cartItems = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            item.setSold(true);
            cartItems.add(item);
        }
        itemRepository.saveAll(cartItems);

        TransactionDetails transactionDetails = new TransactionDetails(merchantId,
                amount,
                phoneNumber.toString(),
                mpesaReceiptNumber);

        transactionDetailsRepository.save(transactionDetails);
        order.setState("paid");
        orderRepository.save(order);

    }

    @GetMapping("/confirm")
    public ResponseEntity confirmOrder(@RequestParam(value = "merchant_request_id") String merchantRequestID) {
        User user = getLoggedInUser();
        Order order = orderRepository.findByUserAndMerchantRequestIDAndState(user, merchantRequestID, "paid");
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("order payment for merchantRequestID : " + merchantRequestID + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity validateOrder(@Valid @org.springframework.web.bind.annotation.RequestBody List<Long> orderList) {
        List<Item> soldList = itemRepository.findBySoldAndIdIn(true, orderList);
        if (!soldList.isEmpty()) {
            return new ResponseEntity<>(soldList, HttpStatus.FOUND);
        }
        int amount = itemRepository.sumOfPriceIn(orderList) / 100;
        try {
            StkPushResponseDto stkPushResponse = triggerStkPush(amount);
            if (stkPushResponse != null) {
                createOrder(getLoggedInUser(), orderList, stkPushResponse);
                return new ResponseEntity<>(stkPushResponse, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Error executing Stk API", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createOrder(User loggedInUser, List<Long> orderList, StkPushResponseDto stkPushResponse) {
        Order order = orderRepository.findByUserAndState(loggedInUser, "pending");

        if (order != null)
            return;

        order = new Order(loggedInUser, "pending", stkPushResponse.getMerchantRequestID());
        orderRepository.save(order);

        List<Item> itemList = itemRepository.findAllById(orderList);

        for (Item item : itemList) {
            OrderItem orderItem = new OrderItem(item, order);
            orderItemRepository.save(orderItem);
        }

    }

    private StkPushResponseDto triggerStkPush(int amount) throws IOException {
        StkPushResponseDto stkPushResponse = null;
        User user = getLoggedInUser();


        long shortCode = Long.parseLong(BUSINESS_SHORT_CODE);

        String userNumber = user.getContactPhoneNumber().substring(1);
        Long userNumberLong = Long.parseLong("254" + userNumber);

        String auth = Base64.getEncoder().encodeToString(secret.getBytes());

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(DARAJA_AUTH_URL)
                .method("GET", null)
                .addHeader("Authorization", "Basic " + auth)
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String resBody = response.body().string();
            DarajaAuthDto darajaAuthDTO = new Gson().fromJson(resBody, DarajaAuthDto.class);
            String darajaAuth = darajaAuthDTO.getAccessToken();
            String callbackUrl = BASE_URL + "order/payment-confirmed";
            System.out.println("STK_CALLBACK_URL:"+callbackUrl);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");

            String timeStamp = sdf.format(timestamp);
            String password = Base64.getEncoder().encodeToString((String.valueOf(shortCode) + DARAJA_PASSKEY + timeStamp).getBytes());

            MediaType mediaType = MediaType.parse("application/json");


            StkPushRequestDto body = new StkPushRequestDto();
            body.setBusinessShortCode((int) shortCode);
            body.setPassword(password);
            body.setTimestamp(timeStamp);
            body.setTransactionType(STK_TRANSACTION_TYPE);
            body.setAmount(amount);
            body.setPartyB(shortCode);
            body.setCallBackURL(callbackUrl);
            body.setPartyA(userNumberLong);
            body.setPhoneNumber(userNumberLong);
            body.setAccountReference(STK_ACCOUNT_REFERENCE);
            body.setTransactionDesc("Payment");

            Request req = new Request.Builder()
                    .url(DARAJA_STK_URL)
                    .method("POST", RequestBody.create(mediaType, new Gson().toJson(body)))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + darajaAuth)
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response res = client.newCall(req).execute();
            String responseBody = res.body().string();

            if (res.isSuccessful()) {
                stkPushResponse = new Gson().fromJson(responseBody, StkPushResponseDto.class);
            } else {
                System.out.println(responseBody);
            }
        }
        return stkPushResponse;
    }
}