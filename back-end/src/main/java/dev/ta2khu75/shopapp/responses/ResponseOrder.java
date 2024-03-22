 package dev.ta2khu75.shopapp.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseOrder {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    @JsonProperty("order_date")
    private LocalDateTime orderDate;
    private String status;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    private Boolean active;// thuộc về admin
    @JsonProperty("order_details")
    private List<ResponseOrderDetail> orderDetails;
    public ResponseOrder(Order order) {
        id = order.getId();
        userId = order.getUser().getId();
        fullName = order.getFullName();
        email = order.getEmail();
        phoneNumber = order.getPhoneNumber();
        address = order.getAddress();
        note = order.getNote();
        orderDate = order.getOrderDate();
        status = order.getStatus();
        totalMoney = order.getTotalMoney();
        shippingMethod = order.getShippingMethod();
        shippingAddress = order.getShippingAddress();
        shippingDate = order.getShippingDate();
        trackingNumber = order.getTrackingNumber();
        paymentMethod = order.getPaymentMethod();
        active = order.getActive();
    }
}