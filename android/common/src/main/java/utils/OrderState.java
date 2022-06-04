package utils;

public enum OrderState {
    PENDING, PAID, PREPARING, READY, ENROUTE, DELIVERED, CLOSED, CANCELLED;

    public static OrderState getOrderState(String value) {
        for (OrderState orderState : OrderState.values()) {
            if (orderState.name().equalsIgnoreCase(value)) return orderState;
        }
        return null;
    }
}
