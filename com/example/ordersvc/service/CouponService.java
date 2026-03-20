package com.example.ordersvc.service;

import com.example.ordersvc.dto.CouponRequest;
import com.example.ordersvc.exception.CouponNotFoundException;
import com.example.ordersvc.model.Coupon;
import com.example.ordersvc.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service for managing coupons.
 */
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * Constructs a new CouponService instance.
     *
     * @param couponRepository the repository for managing coupons
     */
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /**
     * Validates a coupon by its code.
     *
     * @param code the unique code of the coupon
     * @return the coupon if valid, or null otherwise
     */
    @Transactional(readOnly = true)
    public Coupon validateCoupon(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with code: " + code));
    }

    /**
     * Applies a coupon to an order.
     *
     * @param code the unique code of the coupon
     * @param orderTotal the total amount of the order
     * @return the discounted amount if the coupon is applicable, or null otherwise
     */
    @Transactional(readOnly = true)
    public BigDecimal applyCoupon(String code, BigDecimal orderTotal) {
        Coupon coupon = validateCoupon(code);
        if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
            return orderTotal.multiply(coupon.getDiscountValue().divide(BigDecimal.valueOf(100)));
        } else {
            return coupon.getDiscountValue();
        }
    }

    /**
     * Increments the usage count of a coupon.
     *
     * @param code the unique code of the coupon
     */
    @Transactional
    public void incrementUsage(String code) {
        Coupon coupon = validateCoupon(code);
        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        couponRepository.save(coupon);
    }

    /**
     * Creates a new coupon.
     *
     * @param request the request containing the coupon details
     * @return the created coupon
     */
    @Transactional
    public Coupon createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setMaxUses(request.getMaxUses());
        coupon.setCurrentUses(0);
        return couponRepository.save(coupon);
    }

    /**
     * Updates an existing coupon.
     *
     * @param code the unique code of the coupon
     * @param request the request containing the updated coupon details
     * @return the updated coupon
     */
    @Transactional
    public Coupon updateCoupon(String code, CouponRequest request) {
        Coupon coupon = validateCoupon(code);
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setMaxUses(request.getMaxUses());
        return couponRepository.save(coupon);
    }

    /**
     * Deletes a coupon by its code.
     *
     * @param code the unique code of the coupon
     */
    @Transactional
    public void deleteCoupon(String code) {
        Coupon coupon = validateCoupon(code);
        couponRepository.delete(coupon);
    }
}