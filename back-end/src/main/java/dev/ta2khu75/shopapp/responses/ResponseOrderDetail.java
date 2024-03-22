package dev.ta2khu75.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.OrderDetail;
import dev.ta2khu75.shopapp.models.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseOrderDetail {
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private long productId;
    @JsonProperty("product_name")
    private String productName;
    private String thumbnail;
    private Float price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("total_money")
    private Float totalMoney;
    private String color;

    public ResponseOrderDetail(OrderDetail orderDetail) {
        id = orderDetail.getId();
        orderId = orderDetail.getOrder().getId();
        productId=orderDetail.getProduct().getId();
        productName=orderDetail.getProduct().getName();
        thumbnail = orderDetail.getProduct().getThumbnail();
        price = orderDetail.getPrice();
        numberOfProducts = orderDetail.getNumberOfProducts();
        totalMoney = orderDetail.getTotalMoney();
        color = orderDetail.getColor();
    }
}
