package dev.ta2khu75.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoRefreshToken {
  @JsonProperty("refresh_token")
  private String refreshToken;
}
