package dev.ta2khu75.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name="product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;

  private String content;
}
