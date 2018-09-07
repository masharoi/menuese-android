package com.creations.roitman.menume.data;


import java.util.List;

/**
 * Represents the order in the restaurant.
 */

public class Order {

    private int orderId;
    private String restName;
    private int paymentOption;
    private int orderStatus;
    private List<DishItem> items;
    private int restId;
    private double total;
    private String timestamp;


    /**
     * Public constructor.
     * @param orderId of the order
     * @param restName of the restaurant
     * @param paymentOption of the order
     * @param orderStatus status of the order
     * @param items in the order
     */

    public Order(int orderId, String restName, int paymentOption, int orderStatus,
                 List<DishItem> items, double total, String timestamp) {
        this.orderId = orderId;
        this.restName = restName;
        this.paymentOption = paymentOption;
        this.orderStatus = orderStatus;
        this.items = items;
        this.total = total;
        this.timestamp = timestamp;
    }

    /**
     * Public constructor.
     * @param restName of the restaurant
     * @param paymentOption of the order
     * @param orderStatus status of the order
     * @param items in the order
     */
    public Order(String restName, int paymentOption, int orderStatus, List<DishItem> items) {
        this.restName = restName;
        this.paymentOption = paymentOption;
        this.orderStatus = orderStatus;
        this.items = items;
    }

    /**
     * Public constructor.
     * @param restId of the restaurant
     * @param paymentOption of the order
     * @param orderStatus status of the order
     * @param items in the order
     */
    public Order(int restId, int paymentOption, int orderStatus, List<DishItem> items) {
        this.restId = restId;
        this.paymentOption = paymentOption;
        this.orderStatus = orderStatus;
        this.items = items;
    }

    /**
     * Get the timestamp of the order.
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp of the order.
     * @param timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Set the total price of the order.
     * @param total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Get the total price of the order.
     * @return the total price
     */
    public double getTotal() {
        return total;
    }

    /**
     * Get the id of the restaurant.
     * @return the id
     */
    public int getRestId() {
        return restId;
    }

    /**
     * Set the id of the restaurant.
     * @param restId id of the restaurant
     */
    public void setRestId(int restId) {
        this.restId = restId;
    }

    /**
     * Get the items in the order.
     * @return the list of dishes
     */
    public List<DishItem> getItems() {
        return items;
    }

    /**
     * Set the items in the order.
     * @param items in the order
     */
    public void setItems(List<DishItem> items) {
        this.items = items;
    }

    /**
     * Get the id of the order.
     * @return id of the order
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the id of the order.
     * @param id of the order
     */
    public void setOrderId(int id) {
        this.orderId = id;
    }

    /**
     * Get the name of the order.
     * @return name of the order
     */
    public String getRestName() {
        return restName;
    }

    /**
     * Sets the name of the order.
     * @param name of the order
     */
    public void setRestName(String name) {
        this.restName = name;
    }

    /**
     * Gets the payment option (cash=0 or credit card=1).
     * @return integer that represents that value
     */
    public int getPaymentOption() {
        return this.paymentOption;
    }

    /**
     * Sets the payment option.
     * @param option of the payment
     */
    public void setPaymentOption(int option) {
        this.paymentOption = option;
    }

    /**
     * Gets the status of the order.
     * @return the status of the order
     */
    public int getOrderStatus() {
        return this.orderStatus;
    }

    /**
     * Sets the status of the order.
     * @param status of the order
     */
    public void setOrderStatus(int status) {
        this.orderStatus = status;
    }


}
