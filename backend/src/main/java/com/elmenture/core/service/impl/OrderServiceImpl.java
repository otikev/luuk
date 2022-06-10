package com.elmenture.core.service.impl;

import com.elmenture.core.exception.ResourceNotFoundException;
import com.elmenture.core.model.*;
import com.elmenture.core.payload.*;
import com.elmenture.core.repository.*;
import com.elmenture.core.service.EmailService;
import com.elmenture.core.service.OrderItemService;
import com.elmenture.core.service.OrderService;
import com.elmenture.core.utils.OrderState;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.elmenture.core.utils.LuukProperties.*;
import static com.elmenture.core.utils.OrderState.*;

/**
 * Created by otikev on 23-Apr-2022
 */

//https://www.baeldung.com/spring-email
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemPropertyRepository itemPropertyRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    protected EmailService emailService;

    static final String COUNTRY_CODE = "254";
    static final String STK_CALLBACK_URL = BASE_URL + "order/payment-confirmed";

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
        body.setTransactionType("CustomerPayBillOnline");
        body.setAmount(amount);
        body.setPartyB(shortCode);
        body.setCallBackURL(STK_CALLBACK_URL);
        body.setPartyA(userNumberLong);
        body.setPhoneNumber(userNumberLong);
        body.setAccountReference("Luuk Commerce Ltd");
        body.setTransactionDesc("Payment");

        Request req = new Request.Builder()
                .url(DARAJA_STK_URL)
                .method("POST", RequestBody.create(mediaType, new Gson().toJson(body)))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + getAuthentication(DARAJA_AUTH_URL, CONSUMER_KEY + ":" + CONSUMER_SECRET))
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
        Order order = new Order(loggedInUser, PENDING.toString(), stkPushResponse.getMerchantRequestID());
        orderRepository.save(order);
        List<Item> itemList = itemRepository.findAllById(orderList);
        orderItemService.saveOrderItems(order, itemList);
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
        order.setState(PAID.toString());
        orderRepository.save(order);
    }

    private String getAuthentication(String darajaAuthUrl, String secret) throws IOException {
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
    public void cancelOrder(Order order) {
        order.setState(CANCELLED.toString());
        orderRepository.save(order);
    }

    @Override
    public ResponseEntity validateOrder(List<Long> orderList, User loggedInUser) {

        Order pendingOrder = orderRepository.findByUserAndState(loggedInUser, PENDING.toString());

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
        Order order = orderRepository.findByUserAndMerchantRequestIDAndState(user, merchantRequestID, PAID.toString());
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("order payment for merchantRequestID : " + merchantRequestID + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<ItemDto> getItems(int orderId) {
        orderItemRepository.findByOrderId(orderId);
        List<ItemDto> itemDtoList = orderItemRepository.findByOrderId(orderId).stream().map(orderItem -> mapToDTO(orderItem.getItem()))
                .collect(Collectors.toList());
        return itemDtoList;
    }

    @Override
    public void sendNewOrderEmail(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<Integer> externalItemIds = new ArrayList<>();
        long totalCents = 0;
        String customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        String address = order.getUser().getPhysicalAddress();

        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            totalCents += item.getPrice();
            externalItemIds.add(item.getExternalId());
        }

        try {
            emailService.sendNewOrderEmail(order.getId(), externalItemIds, totalCents, customerName, address, "Standard");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getOrdersByState(OrderState orderState) {
        return orderRepository.findAllByState(orderState.toString(), Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public void updateOrderState(OrderStateUpdate stateUpdate) throws ResourceNotFoundException {
        OrderState newState = OrderState.valueOf(stateUpdate.getNewState());

        Optional<Order> order = orderRepository.findById(stateUpdate.getOrderId());

        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Could not find order id " + stateUpdate.getOrderId());
        }

        if (newState == null) {
            throw new IllegalArgumentException("State not found : " + stateUpdate.getNewState());
        }


        Order _order = order.get();
        _order.setState(stateUpdate.getNewState());
        System.out.println("Updating order " + _order.getId() + "'s state to " + newState);
        orderRepository.save(_order);
    }

    @Override
    public Order getPaidOrderForItemId(Long itemId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsForItemId(itemId);
        for (OrderItem orderItem : orderItems) {
            Order order = orderItem.getOrder();
            if (order.isPaid()) {
                return order;
            }
        }
        return null;
    }

    private ItemDto mapToDTO(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setExternalId(item.getExternalId());
        itemDto.setBrand(item.getBrand());
        itemDto.setDescription(item.getDescription());
        itemDto.setSizeInternational(item.getSizeInternational());
        itemDto.setSizeNumber(item.getSizeNumber());
        itemDto.setPrice(item.getPrice());
        itemDto.setTarget(item.getTarget());
        itemDto.setSold(item.getSold());
        itemDto.setSizeType(item.getSizeType());
        itemDto.setImageUrl(item.getImageUrl());

        List<ItemProperty> properties = itemPropertyRepository.findByItemId(item.getId());
        List<Long> ids = new ArrayList<>();
        for (ItemProperty property : properties) {
            ids.add(property.getTagPropertyId());
        }
        itemDto.setTagProperties(ids);
        return itemDto;
    }
}