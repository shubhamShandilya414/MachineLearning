package com.example.ordersvc.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a coupon that can be applied to an order.
 */
@Entity
@Table(name = "coupons")
public class Coupon {

    /**
     * Unique identifier for the coupon.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique code for the coupon.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Coupon code is required")
    private String code;

    /**
     * Type of discount offered by the coupon (PERCENTAGE or FLAT_AMOUNT).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    /**
     * Value of the discount (e.g., 20 for 20% or 10.00 for $10 off).
     */
    @Column(nullable = false)
    private BigDecimal discountValue;

    /**
     * Minimum order amount required to apply the coupon.
     */
    @Column(nullable = false)
    private BigDecimal minOrderAmount;

    /**
     * Maximum number of times the coupon can be used.
     */
    @Column(nullable = false)
    private int maxUses;

    /**
     * Current number of times the coupon has been used.
     */
    @Column(nullable = false)
    private int currentUses;

    /**
     * Whether the coupon is active or not.
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * Constructor for creating a new coupon.
     */
    public Coupon() {
    }

    /**
     * Constructor for creating a new coupon with the given parameters.
     *
     * @param code           Unique code for the coupon
     * @param discountType   Type of discount offered by the coupon
     * @param discountValue  Value of the discount
     * @param minOrderAmount Minimum order amount required to apply the coupon
     * @param maxUses        Maximum number of times the coupon can be used
     */
    public Coupon(String code, DiscountType discountType, BigDecimal discountValue, BigDecimal minOrderAmount, int maxUses) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxUses = maxUses;
        this.currentUses = 0;
        this.active = true;
    }

    /**
     * Gets the unique identifier for the coupon.
     *
     * @return Unique identifier for the coupon
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the coupon.
     *
     * @param id Unique identifier for the coupon
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the unique code for the coupon.
     *
     * @return Unique code for the coupon
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the unique code for the coupon.
     *
     * @param code Unique code for the coupon
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the type of discount offered by the coupon.
     *
     * @return Type of discount offered by the coupon
     */
    public DiscountType getDiscountType() {
        return discountType;
    }

    /**
     * Sets the type of discount offered by the coupon.
     *
     * @param discountType Type of discount offered by the coupon
     */
    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    /**
     * Gets the value of the discount.
     *
     * @return Value of the discount
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * Sets the value of the discount.
     *
     * @param discountValue Value of the discount
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * Gets the minimum order amount required to apply the coupon.
     *
     * @return Minimum order amount required to apply the coupon
     */
    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    /**
     * Sets the minimum order amount required to apply the coupon.
     *
     * @param minOrderAmount Minimum order amount required to apply the coupon
     */
    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    /**
     * Gets the maximum number of times the coupon can be used.
     *
     * @return Maximum number of times the coupon can be used
     */
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * Sets the maximum number of times the coupon can be used.
     *
     * @param maxUses Maximum number of times the coupon can be used
     */
    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    /**
     * Gets the current number of times the coupon has been used.
     *
     * @return Current number of times the coupon has been used
     */
    public int getCurrentUses() {
        return currentUses;
    }

    /**
     * Sets the current number of times the coupon has been used.
     *
     * @param currentUses Current number of times the coupon has been used
     */
    public void setCurrentUses(int currentUses) {
        this.currentUses = currentUses;
    }

    /**
     * Gets whether the coupon is active or not.
     *
     * @return Whether the coupon is active or not
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the coupon is active or not.
     *
     * @param active Whether the coupon is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id) &&
                Objects.equals(code, coupon.code) &&
                discountType == coupon.discountType &&
                Objects.equals(discountValue, coupon.discountValue) &&
                Objects.equals(minOrderAmount, coupon.minOrderAmount) &&
                maxUses == coupon.maxUses &&
                currentUses == coupon.currentUses &&
                active == coupon.active;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, discountType, discountValue, minOrderAmount, maxUses, currentUses, active);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                ", minOrderAmount=" + minOrderAmount +
                ", maxUses=" + maxUses +
                ", currentUses=" + currentUses +
                ", active=" + active +
                '}';
    }

    /**
     * Enum for the type of discount offered by the coupon.
     */
    public enum DiscountType {
        /**
         * Percentage discount (e.g., 20% off).
         */
        PERCENTAGE,
        /**
         * Flat amount discount (e.g., $10 off).
         */
        FLAT_AMOUNT
    }
}