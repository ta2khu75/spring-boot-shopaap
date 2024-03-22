package dev.ta2khu75.shopapp.services.iservices;

import java.util.List;

import dev.ta2khu75.shopapp.dtos.DtoOrderDetail;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.responses.ResponseOrderDetail;

public interface IServiceOrderDetail {
    ResponseOrderDetail createOrderDetail(DtoOrderDetail dto) throws DataNotFoundException;

    ResponseOrderDetail getOrderDetailById(long id) throws DataNotFoundException;

    ResponseOrderDetail updateOrderDetail(long id, DtoOrderDetail dto) throws DataNotFoundException;

    void deleteOrderDetail(long id);

    List<ResponseOrderDetail> getAllOrders(Long order_id);
}
