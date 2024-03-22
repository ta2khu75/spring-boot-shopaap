package dev.ta2khu75.shopapp.responses;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.Role;
import dev.ta2khu75.shopapp.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseUser {
  private long id;
  @JsonProperty("full_name")
  private String fullName;
  @JsonProperty("phone_number")
  private String phoneNumber;
  private String address;
  @JsonProperty("date_of_birth")
  private LocalDate dateOfBirth;
  @JsonProperty("facebook_account_id")
  private int facebookAccountId;
  @JsonProperty("google_account_id")
  private int googleAccountId;
  private Role role;
  public ResponseUser(User user){
     id=user.getId();
     fullName=user.getFullName();
     phoneNumber=user.getPhoneNumber();
     address=user.getAddress();
     dateOfBirth=user.getDateOfBirth();
     facebookAccountId=user.getFacebookAccountId();
     googleAccountId=user.getGoogleAccountId();
     role=user.getRole();
  } 
}
