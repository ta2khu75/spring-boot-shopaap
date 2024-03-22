package dev.ta2khu75.shopapp.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DtoUserUpdate {
  private long id;
  @JsonProperty("fullname")
  private String fullName;
  private String address;
  private String password;
  @JsonProperty("retype_password")
  private String retypePassword;
  @JsonProperty("date_of_birth")
  private LocalDate dateOfBirth;
  @JsonProperty("facebook_account_id")
  private int facebookAccountId;
  @JsonProperty("google_account_id")
  private int googleAccountId;
}
