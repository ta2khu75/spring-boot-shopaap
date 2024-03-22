package dev.ta2khu75.shopapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    private boolean revoked;
    private boolean expired;
    @Column(name = "is_mobile")
    private boolean mobile;
    @Column(name="refresh_token")
    private String refreshToken;
    @Column(name="refresh_expiration_data")
    private LocalDateTime refreshExpirationDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
