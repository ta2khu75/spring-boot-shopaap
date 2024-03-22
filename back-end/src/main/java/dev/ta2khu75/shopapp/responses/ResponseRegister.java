package dev.ta2khu75.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.User;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseRegister {
  @JsonProperty("message")
  private String message;
  @JsonProperty("user")
  private User user;
}