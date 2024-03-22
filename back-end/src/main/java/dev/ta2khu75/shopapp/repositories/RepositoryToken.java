package dev.ta2khu75.shopapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Token;
import dev.ta2khu75.shopapp.models.User;

import java.util.List;


@Repository
public interface RepositoryToken extends JpaRepository<Token, Integer> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Token findByRefreshToken(String refreshToken);
}
