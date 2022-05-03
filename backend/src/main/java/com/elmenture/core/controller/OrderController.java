package com.elmenture.core.controller;

import com.elmenture.core.model.*;
import com.elmenture.core.model.Item;
import com.elmenture.core.payload.*;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.repository.OrderItemRepository;
import com.elmenture.core.repository.OrderRepository;
import com.elmenture.core.repository.TransactionDetailsRepository;
import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
    public ResponseEntity fetchOrders() {
        User user = getLoggedInUser();
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        return new ResponseEntity(orderList, HttpStatus.OK);
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
        } else {
            undoCreatedOrder(order);
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
        String key = "G7v6VB93Ax1f01hvFyokdHT679GMSx7Y";
        String secret = "G7v6VB93Ax1f01hvFyokdHT679GMSx7Y:QD8uVEuhE5GQKHzp";
        String auth = Base64.getEncoder().encodeToString(secret.getBytes());

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .method("GET", null)
                .addHeader("Authorization", "Basic " + auth)
                .build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String resBody = response.body().string();
            DarajaAuthDto darajaAuthDTO = new Gson().fromJson(resBody, DarajaAuthDto.class);
            String darajaAuth = darajaAuthDTO.getAccessToken();
            String callbackUrl = "https://7f2e-41-80-23-108.in.ngrok.io/order/payment-confirmed";

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");

            String passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
            String timeStamp = sdf.format(timestamp);
            String shortCode = "174379";
            String password = Base64.getEncoder().encodeToString((shortCode + passkey + timeStamp).getBytes());

            MediaType mediaType = MediaType.parse("application/json");

            StkPushRequestDto body = new StkPushRequestDto();
            body.setBusinessShortCode(174379);
            body.setPassword(password);
            body.setTimestamp(timeStamp);
            body.setTransactionType("CustomerPayBillOnline");
            body.setAmount(1/*amount*/);
            body.setPartyB(174379L);
            body.setCallBackURL(callbackUrl);
            body.setPartyA(254704033136L);
            body.setPhoneNumber(254704033136L);
            body.setAccountReference("LuukAtMe");
            body.setTransactionDesc("Payment");


            Request req = new Request.Builder()
                    .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                    .method("POST", RequestBody.create(mediaType, new Gson().toJson(body)))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + darajaAuth)
                    .build();

            Response res = client.newCall(req).execute();
            if (res.isSuccessful()) {
                String responseBody = res.body().string();
                stkPushResponse = new Gson().fromJson(responseBody, StkPushResponseDto.class);
            }
        }
        return stkPushResponse;
    }

}