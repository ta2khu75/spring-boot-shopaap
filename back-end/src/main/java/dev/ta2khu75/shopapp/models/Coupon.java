package dev.ta2khu75.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coupon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  Long id;

  @Column(name="code", nullable=false, unique=true)
  private String code;

  @Column(name="active", nullable = false)
  private boolean active;

//   INSERT INTO coupons (id, code) VALUES (1, 'HEAVEN');
// INSERT INTO Coupons (id, code) VALUES (2, 'DISCOUNT20');
}
