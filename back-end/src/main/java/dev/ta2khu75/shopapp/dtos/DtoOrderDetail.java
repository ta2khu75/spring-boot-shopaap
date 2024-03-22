package dev.ta2khu75.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.Product;
import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DtoOrderDetail {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's ID must be > 0")
    private long orderId;
    @Min(value = 1, message = "Product's ID must be >0")
    @JsonProperty("product_id")
    private long productId;
    @Min(value = 0, message = "Product's price must be >=0")
    private float price;
    @JsonProperty("number_of_products")
    @Min(value = 0, message = "number of products must be >=1")
    private int numberOfProducts;
    @JsonProperty("total_money")
    @Min(value = 0, message = "total money must be >=0")
    private float totalMoney;
    private String color;
    public DtoOrderDetail(long orderId, Product product, int quantity){
            this.orderId=orderId;
            this.productId=product.getId();
            this.price=product.getPrice();
            numberOfProducts=quantity;
    }
}
