package dev.ta2khu75.shopapp.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.dtos.DtoOrderDetail;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Order;
import dev.ta2khu75.shopapp.models.OrderDetail;
import dev.ta2khu75.shopapp.models.Product;
import dev.ta2khu75.shopapp.repositories.RepositoryOrder;
import dev.ta2khu75.shopapp.repositories.RepositoryOrderDetail;
import dev.ta2khu75.shopapp.repositories.RepositoryProduct;
import dev.ta2khu75.shopapp.responses.ResponseOrderDetail;
import dev.ta2khu75.shopapp.services.iservices.IServiceOrderDetail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ServiceOrderDetail implements IServiceOrderDetail {
    private final RepositoryOrder repositoryOrder;
    private final RepositoryOrderDetail repositoryOrderDetail;
    private final RepositoryProduct repositoryProduct;
    private final ModelMapper modelMapper;

    @Override
    public ResponseOrderDetail createOrderDetail(DtoOrderDetail dto) throws DataNotFoundException {
        Order order = repositoryOrder.findById(dto.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order id:" + dto.getOrderId()));
        Product product = repositoryProduct.findById(dto.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order id:" + dto.getOrderId()));
        // TODO Auto-generated method stub
        OrderDetail orderDetail = OrderDetail.builder()
                                                .order(order)
                                                .product(product)
                                                .price(dto.getPrice())
                                                .numberOfProducts(dto.getNumberOfProducts())
                                                .totalMoney(dto.getTotalMoney())
                                                .color(dto.getColor())
                                                .build();
        orderDetail = repositoryOrderDetail.save(orderDetail);
        ResponseOrderDetail responseOrderDetail = new ResponseOrderDetail(orderDetail);
        return responseOrderDetail;
    }

    @Override
    public ResponseOrderDetail getOrderDetailById(long id) throws DataNotFoundException {
        // TODO Auto-generated method stub
        OrderDetail orderDetail = repositoryOrderDetail.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot not found Order_Detail id:" + id));
        modelMapper.typeMap(OrderDetail.class, ResponseOrderDetail.class);
        ResponseOrderDetail responseOrderDetail = new ResponseOrderDetail();
        modelMapper.map(orderDetail, responseOrderDetail);
        return responseOrderDetail;
    }

    @Override
    public ResponseOrderDetail updateOrderDetail(long id, DtoOrderDetail dto) throws DataNotFoundException {
        // TODO Auto-generated method stub
        OrderDetail orderDetail=repositoryOrderDetail.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find order_detail id:"+id));
        Order order=repositoryOrder.findById(dto.getOrderId()).orElseThrow(() -> new DataNotFoundException("Cannot find order_detail id:"+id));
        Product product=repositoryProduct.findById(dto.getProductId()).orElseThrow(() -> new DataNotFoundException("Cannot find order_detail id:"+id));
        orderDetail.setPrice(dto.getPrice());
        orderDetail.setTotalMoney(dto.getTotalMoney());
        orderDetail.setColor(dto.getColor());
        orderDetail.setNumberOfProducts(dto.getNumberOfProducts());
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        return new ResponseOrderDetail(repositoryOrderDetail.save(orderDetail));
    }

    @Override
    public void deleteOrderDetail(long id) {
        Optional<OrderDetail> orderDetaiOptional = repositoryOrderDetail.findById(id);
        if (orderDetaiOptional.isPresent()) {
            repositoryOrderDetail.deleteById(id);
        }
    }

    @Override
    public List<ResponseOrderDetail> getAllOrders(Long order_id) {
        // TODO Auto-generated method stub
        List<OrderDetail> listOrderDetails=repositoryOrderDetail.findByOrderId(order_id);
        List<ResponseOrderDetail> listResponseOrderDetails=listOrderDetails.stream().map(arg0 -> new ResponseOrderDetail(arg0)).toList();
        return listResponseOrderDetails;
    }
}
