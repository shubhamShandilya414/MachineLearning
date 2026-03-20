package com.example.ordersvc.repository;

import com.example.ordersvc.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing coupons.
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * Finds a coupon by its unique code.
     *
     * @param code the unique code of the coupon
     * @return the coupon if found, or an empty Optional otherwise
     */
    @Query("SELECT c FROM Coupon c WHERE c.code = :code")
    Optional<Coupon> findByCode(@Param("code") String code);

    /**
     * Finds all active coupons.
     *
     * @return a list of active coupons
     */
    @Query("SELECT c FROM Coupon c WHERE c.active = true")
    Iterable<Coupon> findByActiveTrue();
}