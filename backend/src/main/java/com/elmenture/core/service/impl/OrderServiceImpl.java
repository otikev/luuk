package com.elmenture.core.service.impl;

import com.elmenture.core.model.*;
import com.elmenture.core.payload.DarajaAuthDto;
import com.elmenture.core.payload.OrderConfirmationDto;
import com.elmenture.core.payload.StkPushRequestDto;
import com.elmenture.core.payload.StkPushResponseDto;
import com.elmenture.core.repository.ItemRepository;
import com.elmenture.core.repository.OrderItemRepository;
import com.elmenture.core.repository.OrderRepository;
import com.elmenture.core.repository.TransactionDetailsRepository;
import com.elmenture.core.service.OrderService;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.elmenture.core.utils.LuukProperties.*;

/**
 * Created by otikev on 23-Apr-2022
 */

//https://www.baeldung.com/spring-email
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public StkPushResponseDto triggerStkPush(int amount, User user) throws IOException {
        StkPushResponseDto stkPushResponse = null;

        long shortCode = Long.parseLong(BUSINESS_SHORT_CODE);
        Long userNumberLong = Long.parseLong(COUNTRY_CODE + user.getContactPhoneNumber().substring(1));

        System.out.println("STK_CALLBACK_URL:" + STK_CALLBACK_URL);

        String timeStamp = new SimpleDateFormat("YYYYMMddHHmmss").format(new Timestamp(System.currentTimeMillis()));
        String password = Base64.getEncoder().encodeToString((shortCode + DARAJA_PASSKEY + timeStamp).getBytes());

        MediaType mediaType = MediaType.parse("application/json");
        StkPushRequestDto body = new StkPushRequestDto();
        body.setBusinessShortCode((int) shortCode);
        body.setPassword(password);
        body.setTimestamp(timeStamp);
        body.setTransactionType(STK_TRANSACTION_TYPE);
        body.setAmount(amount);
        body.setPartyB(shortCode);
        body.setCallBackURL(STK_CALLBACK_URL);
        body.setPartyA(userNumberLong);
        body.setPhoneNumber(userNumberLong);
        body.setAccountReference(STK_ACCOUNT_REFERENCE);
        body.setTransactionDesc("Payment");

        Request req = new Request.Builder()
                .url(DARAJA_STK_URL)
                .method("POST", RequestBody.create(mediaType, new Gson().toJson(body)))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + getAuthentication(DARAJA_AUTH_URL, secret))
                .addHeader("cache-control", "no-cache")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Response res = client.newCall(req).execute();
        String responseBody = res.body().string();

        if (res.isSuccessful()) {
            stkPushResponse = new Gson().fromJson(responseBody, StkPushResponseDto.class);
        } else {
            System.out.println("STK Callback:" + responseBody);
        }
        return stkPushResponse;
    }

    @Override
    public void createOrder(User loggedInUser, List<Long> orderList, StkPushResponseDto stkPushResponse) {
        Order order = new Order(loggedInUser, "pending", stkPushResponse.getMerchantRequestID());
        orderRepository.save(order);
        List<Item> itemList = itemRepository.findAllById(orderList);

        for (Item item : itemList) {
            OrderItem orderItem = new OrderItem(item, order);
            orderItemRepository.save(orderItem);
        }
    }

    @Override
    public OrderConfirmationDto getOrderDetailsFromMerchantId(Object request) {
        OrderConfirmationDto orderConfirmationDto = new OrderConfirmationDto();
        String objectJson = new Gson().toJson(request);
        JSONObject body = new JSONObject(objectJson).getJSONObject("Body");

        JSONObject stkCallback = body.getJSONObject("stkCallback");
        String merchantRequestId = stkCallback.getString("MerchantRequestID");
        int resultCode = stkCallback.getInt("ResultCode");


        orderConfirmationDto.setCallbackResultCode(resultCode);
        orderConfirmationDto.setCallbackMerchantId(merchantRequestId);
        orderConfirmationDto.setOrder(orderRepository.findByMerchantRequestID(merchantRequestId));

        if (resultCode != 0) {
            return orderConfirmationDto;
        }

        JSONObject callbackMetadata = stkCallback.getJSONObject("CallbackMetadata");
        JSONArray itemlist = callbackMetadata.getJSONArray("Item");
        double amount = itemlist.getJSONObject(0).getDouble("Value");
        String mpesaReceiptNumber = itemlist.getJSONObject(1).getString("Value");
        Long phoneNumber = itemlist.getJSONObject(4).getLong("Value");


        orderConfirmationDto.setAmount(amount);
        orderConfirmationDto.setMpesaReceiptNumber(mpesaReceiptNumber);
        orderConfirmationDto.setPhoneNumber(phoneNumber);

        return orderConfirmationDto;

    }

    @Override
    public void saveTransactionDetails(OrderConfirmationDto orderConfirmationDto) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderConfirmationDto.getOrder().getId());
        List<Item> cartItems = new ArrayList<>();
        Order order = orderConfirmationDto.getOrder();

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            item.setSold(true);
            cartItems.add(item);
        }
        itemRepository.saveAll(cartItems);

        TransactionDetails transactionDetails = new TransactionDetails(orderConfirmationDto.getCallbackMerchantId(),
                orderConfirmationDto.getAmount(),
                orderConfirmationDto.getPhoneNumber().toString(),
                orderConfirmationDto.getMpesaReceiptNumber());

        transactionDetailsRepository.save(transactionDetails);
        order.setState("paid");
        orderRepository.save(order);
    }

    String getAuthentication(String darajaAuthUrl, String secret) throws IOException {
        String auth = Base64.getEncoder().encodeToString(secret.getBytes());
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(darajaAuthUrl)
                .method("GET", null)
                .addHeader("Authorization", "Basic " + auth)
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String resBody = response.body().string();
            DarajaAuthDto darajaAuthDTO = new Gson().fromJson(resBody, DarajaAuthDto.class);
            String darajaAuth = darajaAuthDTO.getAccessToken();
            System.out.println("Successfully generated daraja authentication");
            return darajaAuth;
        }
        System.out.println("Failed to generate daraja authentication");
        return null;
    }

    @Override
    public void undoCreatedOrder(Order order) {
        orderItemRepository.deleteByOrderId(order.getId());
        orderRepository.delete(order);
    }

    @Override
    public ResponseEntity validateOrder(List<Long> orderList, User loggedInUser) {

        Order pendingOrder = orderRepository.findByUserAndState(loggedInUser, "pending");

        List<Item> soldList = itemRepository.findBySoldAndIdIn(true, orderList);
        if (!soldList.isEmpty()) {
            return new ResponseEntity<>(soldList, HttpStatus.FOUND);
        }

        int amount = itemRepository.sumOfPriceIn(orderList) / 100;

        try {
            StkPushResponseDto stkPushResponse = triggerStkPush(amount, loggedInUser);
            if (stkPushResponse != null) {
                if (pendingOrder == null) {
                    createOrder(loggedInUser, orderList, stkPushResponse);
                }
                return new ResponseEntity<>(stkPushResponse, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Error executing Stk API", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity confirmOrderPaid(User user, String merchantRequestID) {
        Order order = orderRepository.findByUserAndMerchantRequestIDAndState(user, merchantRequestID, "paid");
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("order payment for merchantRequestID : " + merchantRequestID + " does not exist", HttpStatus.NOT_FOUND);
        }
    }
}