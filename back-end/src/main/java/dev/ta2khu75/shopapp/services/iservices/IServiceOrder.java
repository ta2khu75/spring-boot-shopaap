package dev.ta2khu75.shopapp.services.iservices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.ta2khu75.shopapp.dtos.DtoOrder;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Order;
import dev.ta2khu75.shopapp.responses.ResponseOrder;

public interface IServiceOrder {
    ResponseOrder createOrder(DtoOrder Order) throws DataNotFoundException;

    ResponseOrder getOrderById(long id) throws DataNotFoundException;

    ResponseOrder updateOrder(long id, DtoOrder Order) throws DataNotFoundException;

    void deleteOrder(long id);

    List<ResponseOrder> getAllOrdersByUserId(Long userId);

    Page<Order> getAllOrderByKeyword(String keyword, Pageable pageable);
}