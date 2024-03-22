package dev.ta2khu75.shopapp.responses;

import org.springframework.http.HttpStatus;

import dev.ta2khu75.shopapp.models.Product;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseObject {
  private String message;
  private HttpStatus status;
  private Object data;
}
