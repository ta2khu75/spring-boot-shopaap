package dev.ta2khu75.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class DtoProductImage {
    @JsonProperty("product_id")
    @Min(value=1, message="User's Id must be >0")
    private Long productId;

    @Size(min=5, max = 200, message = "Image's name")
    @JsonProperty("image_url")
    private String imageUrl;
}
