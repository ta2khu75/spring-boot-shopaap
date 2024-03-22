package dev.ta2khu75.shopapp.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon_conditions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponCondition {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false)
  @JsonBackReference
  private Coupon coupon;
  @Column(name = "attribute", nullable = false)
  private String attribute;
  @Column(name = "operator", nullable = false)
  private String operator;
  @Column(name = "value", nullable = false)
  private String value;
  @Column(name = "discount_amount", nullable = false)
  private BigDecimal discountAmount;


  // INSERT INTO Coupon_conditions ( coupon_id, attribute, operator, value, discount_amount) VALUES ( 1, 'minimum_amount', '>', '100', 10); 
  // INSERT INTO coupon_conditions ( coupon_id, attribute, operator, value, discount_amount) VALUES (1, 'applicable_date', 'BETWEEN', '2023-12-25', 5);
  // INSERT INTO Coupon_conditions ( coupon_id, attribute, operator, value, discount_amount) VALUES ( 2, 'minimum_amount', '>', '200', 20); 

}
