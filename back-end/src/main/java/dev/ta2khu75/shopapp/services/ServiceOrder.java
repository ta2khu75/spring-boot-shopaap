package dev.ta2khu75.shopapp.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.dtos.DtoCartItem;
import dev.ta2khu75.shopapp.dtos.DtoOrder;
import dev.ta2khu75.shopapp.dtos.DtoOrderDetail;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Order;
import dev.ta2khu75.shopapp.models.OrderDetail;
import dev.ta2khu75.shopapp.models.OrderStatus;
import dev.ta2khu75.shopapp.models.Product;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.repositories.RepositoryOrder;
import dev.ta2khu75.shopapp.repositories.RepositoryOrderDetail;
import dev.ta2khu75.shopapp.repositories.RepositoryUser;
import dev.ta2khu75.shopapp.responses.ResponseOrder;
import dev.ta2khu75.shopapp.responses.ResponseOrderDetail;
import dev.ta2khu75.shopapp.responses.ResponseOrderDetail.ResponseOrderDetailBuilder;
import dev.ta2khu75.shopapp.services.iservices.IServiceOrder;
import dev.ta2khu75.shopapp.services.iservices.IServiceOrderDetail;
import dev.ta2khu75.shopapp.services.iservices.IServiceProduct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceOrder implements IServiceOrder {
    private final RepositoryOrder repositoryOrder;
    private final RepositoryUser repositoryUser;
    private final RepositoryOrderDetail repositoryOrderDetail;
    private final IServiceOrderDetail serviceOrderDetail;
    private final IServiceProduct serviceProduct;
    private final ModelMapper modelMapper;

    @Override
    public ResponseOrder createOrder(DtoOrder dto) throws DataNotFoundException {
        // find user_id exist
        User user = repositoryUser.findById(dto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id:" + dto.getUserId()));
        // TODO Auto-generated method stub
        modelMapper.typeMap(DtoOrder.class, Order.class).addMappings(arg0 -> arg0.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(dto, order);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = dto.getShippingDate() == null ? LocalDate.now() : dto.getShippingDate();
        if (shippingDate == null || shippingDate.isBefore(LocalDate.now()))
            throw new DataNotFoundException("Date must be least today");
        order.setShippingDate(shippingDate);
        order.setActive(true);
        Order orderr = repositoryOrder.save(order);
        List<ResponseOrderDetail> cartItems = new LinkedList<>();
        for (DtoCartItem x : dto.getCartItems()) {
            Product product = serviceProduct.getProductById(x.getProductId());
            DtoOrderDetail dtoOrderDetail = new DtoOrderDetail(orderr.getId(), product, x.getQuatity());
            ResponseOrderDetail responseOrderDetail = serviceOrderDetail.createOrderDetail(dtoOrderDetail);
            cartItems.add(responseOrderDetail);
        }
        modelMapper.typeMap(Order.class, ResponseOrder.class);
        ResponseOrder responseOrder = new ResponseOrder();
        modelMapper.map(orderr, responseOrder);
        responseOrder.setOrderDetails(cartItems);
        return responseOrder;
    }

    @Override
    public ResponseOrder getOrderById(long id) throws DataNotFoundException {
        // TODO Auto-generated method stub
        modelMapper.typeMap(Order.class, ResponseOrder.class);
        ResponseOrder responseOrder = new ResponseOrder();
        Order order = repositoryOrder.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order id:" + id));
        modelMapper.map(order, responseOrder);
        responseOrder.setOrderDetails(serviceOrderDetail.getAllOrders(responseOrder.getId()));
        return responseOrder;
    }

    @Override
    public ResponseOrder updateOrder(long id, DtoOrder dto) throws DataNotFoundException {
        Order order = repositoryOrder.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found order id:" + id));
        User user = repositoryUser.findById(dto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id:" + id));
        modelMapper.typeMap(DtoOrder.class, Order.class).addMappings(source -> source.skip(Order::setId));
        modelMapper.map(dto, order);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order = repositoryOrder.save(order);
        modelMapper.typeMap(Order.class, ResponseOrder.class);
        ResponseOrder responseOrder = new ResponseOrder();
        modelMapper.map(order, responseOrder);
        return responseOrder;
    }

    @Override
    public void deleteOrder(long id) {
        Order order = repositoryOrder.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            repositoryOrder.save(order);
        }
    }

    @Override
    public List<ResponseOrder> getAllOrdersByUserId(Long user) {
        List<Order> listOrder = repositoryOrder.findByUserId(user);
        List<ResponseOrder> listReponse = listOrder.stream().map(arg0 -> modelMapper.map(arg0, ResponseOrder.class))
                .collect(Collectors.toList());
        // TODO Auto-generated method stub
        return listReponse;
    }

    @Override
    public Page<Order> getAllOrderByKeyword(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        Page<Order> pageOrder = repositoryOrder.findByKeyword(keyword, pageable);
        return pageOrder;
    }

}
