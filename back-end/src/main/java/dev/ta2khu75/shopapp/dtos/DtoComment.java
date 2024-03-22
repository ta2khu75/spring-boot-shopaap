package dev.ta2khu75.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DtoComment {
  @JsonProperty("product_id")
  private Long productId;

  @JsonProperty("user_id")
  private Long userId;
  
  private String content;

}
