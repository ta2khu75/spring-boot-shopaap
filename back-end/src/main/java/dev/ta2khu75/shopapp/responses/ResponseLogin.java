package dev.ta2khu75.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseLogin {
  @JsonProperty("message")
  private String message;
  @JsonProperty("token")
  private String token;
  @JsonProperty("refresh_token")
  private String refreshToken;
}
