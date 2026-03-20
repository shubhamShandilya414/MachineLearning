package com.example.ordersvc.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Request object for creating a new order.
 */
public class CreateOrderRequest {

    /**
     * Email address of the customer placing the order.
     */
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    /**
     * Name of the customer placing the order.
     */
    @NotBlank(message = "Customer name is required")
    private String customerName;

    /**
     * Shipping address for the order.
     */
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    /**
     * List of items in the order.
     */
    @NotEmpty(message = "At least one order item is required")
    private List<OrderItemRequest> items;

    /**
     * Optional coupon code to apply to the order.
     */
    private String couponCode;

    /**
     * Gets the customer email.
     *
     * @return the customer email
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Sets the customer email.
     *
     * @param customerEmail the customer email to set
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * Gets the customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     *
     * @param customerName the customer name to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the shipping address.
     *
     * @return the shipping address
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Sets the shipping address.
     *
     * @param shippingAddress the shipping address to set
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Gets the list of order items.
     *
     * @return the list of order items
     */
    public List<OrderItemRequest> getItems() {
        return items;
    }

    /**
     * Sets the list of order items.
     *
     * @param items the list of order items to set
     */
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    /**
     * Gets the coupon code.
     *
     * @return the coupon code
     */
    public String getCouponCode() {
        return couponCode;
    }

    /**
     * Sets the coupon code.
     *
     * @param couponCode the coupon code to set
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}