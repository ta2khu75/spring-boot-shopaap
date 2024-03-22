package dev.ta2khu75.shopapp.dtos;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DtoOrder {
    @JsonProperty("user_id")
    @Min(value=1, message="must have user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    private String email;
    @JsonProperty("phone_number")
    @NotBlank(message = "must have phoneNumber")
    @Size(min = 10, max=10, message = "phone number must have 10 lengths")
    private String phoneNumber;
    private String address;
    private String note;
    private String status;
    @JsonProperty("total_money")
    @Min(value = 0, message = "total totalMoney must be >=0")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("cart_items")
    private List<DtoCartItem> cartItems;
}
