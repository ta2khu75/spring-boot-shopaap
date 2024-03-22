package dev.ta2khu75.shopapp.responses;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseCouponCalculate {
  private double result;
  private String message;
}
