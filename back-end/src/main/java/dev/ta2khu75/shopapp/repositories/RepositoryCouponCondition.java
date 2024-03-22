package dev.ta2khu75.shopapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.CouponCondition;
import java.util.List;


@Repository
public interface RepositoryCouponCondition extends JpaRepository<CouponCondition, Long> {
  List<CouponCondition> findByCouponId(Long couponId);
}
