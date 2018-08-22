package com.creations.roitman.menume.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Order {
    @NonNull
    @PrimaryKey
    private int orderId;
    private int restId;
    private int paymentOption;
    private int orderStatus;


    /**
     * Public constructor.
     * @param orderId of the order
     * @param restId of the restaurant
     * @param paymentOption of the order
     * @param orderStatus status of the order
     */
    @Ignore
    public Order(int orderId, int restId, int paymentOption, int orderStatus) {
        this.orderId = orderId;
        this.restId = restId;
        this.paymentOption = paymentOption;
        this.orderStatus = orderStatus;
    }

    /**
     * Public constructor.
     * @param restId of the restaurant
     * @param paymentOption of the order
     * @param orderStatus status of the order
     */
    public Order(int restId, int paymentOption, int orderStatus) {
        this.restId = restId;
        this.paymentOption = paymentOption;
        this.orderStatus = orderStatus;
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
     * Get the id of the order.
     * @return id of the order
     */
    public int getRestId() {
        return restId;
    }

    /**
     * Sets the id of the order.
     * @param id of the order
     */
    public void setRestId(int id) {
        this.restId = id;
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
