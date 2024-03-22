package dev.ta2khu75.shopapp.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.OrderDetail;

@Repository
public interface RepositoryOrderDetail extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
