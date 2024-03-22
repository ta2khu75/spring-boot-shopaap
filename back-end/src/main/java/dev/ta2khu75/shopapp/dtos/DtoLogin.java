package dev.ta2khu75.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DtoLogin {
    @JsonProperty("phone_number")
    @NotBlank(message = "phải nhập số điện thoại")
    private String phoneNumber;
    @NotBlank(message = "Phải nhập mật khẩu")
    private String password;
    @JsonProperty("role_id")
    private Integer roleId;    
}
