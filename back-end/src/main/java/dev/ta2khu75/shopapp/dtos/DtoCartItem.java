package dev.ta2khu75.shopapp.dtos;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DtoCartItem implements Serializable {
    @JsonProperty("product_id")
    private long productId;
    @JsonProperty("quantity")
    private int quatity;
}