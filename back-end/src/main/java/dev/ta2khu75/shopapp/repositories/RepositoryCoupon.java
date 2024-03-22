package dev.ta2khu75.shopapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Coupon;

@Repository
public interface RepositoryCoupon extends JpaRepository<Coupon, Long> {
  Optional<Coupon> findByCode(String couponCode);
}
