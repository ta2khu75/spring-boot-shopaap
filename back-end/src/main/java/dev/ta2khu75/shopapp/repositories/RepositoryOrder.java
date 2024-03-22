package dev.ta2khu75.shopapp.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Order;

@Repository
public interface RepositoryOrder extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    @Query("SELECT o FROM Order o WHERE o.active=true AND (:keyword is null or :keyword='' or o.fullName like %:keyword% or o.address like %:keyword% or o.note like %:keyword% or o.email like %:keyword%)")
    Page<Order> findByKeyword(String keyword, Pageable pageable);
}
