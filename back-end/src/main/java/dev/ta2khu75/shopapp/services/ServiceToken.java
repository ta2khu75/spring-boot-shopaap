package dev.ta2khu75.shopapp.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.component.JwtTokenUtil;
import dev.ta2khu75.shopapp.models.Token;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.repositories.RepositoryToken;
import dev.ta2khu75.shopapp.services.iservices.IServiceToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceToken implements IServiceToken {
  private static final int MAX_TOKENS = 3;
  @Value("${jwt.expiration}")
  private int expiration;
  @Value("${jwt.refresh.expiration}")
  private int refreshExpiration;
  private final RepositoryToken repositoryToken;
  private final JwtTokenUtil jwtTokenUtil;
  @Override
  public Token addToken(User user, String token, boolean isMobileDevice) {
    List<Token> userTokens = repositoryToken.findByUser(user);
    int tokenCount = userTokens.size();
    if (tokenCount >= MAX_TOKENS) {
      Token tokenToDelete;
      boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
      if (hasNonMobileToken) {
        tokenToDelete = userTokens.stream().filter(userToken -> !userToken.isMobile()).findFirst()
            .orElse(userTokens.get(0));
      } else {
        tokenToDelete = userTokens.get(0);
      }
      repositoryToken.delete(tokenToDelete);
    }
    LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
    Token newToken = Token.builder().user(user).token(token).revoked(false).expired(false).tokenType("Bearer")
        .expirationDate(expirationDateTime).mobile(isMobileDevice).build();

    newToken.setRefreshToken(UUID.randomUUID().toString());
    newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(refreshExpiration));
    return repositoryToken.save(newToken);
  }

  @Override
  public Token refreshToken(String refreshToken, User user) throws Exception {
    // TODO Auto-generated method stub
    Token token=repositoryToken.findByRefreshToken(refreshToken);
    token.setToken(jwtTokenUtil.generateToken(user));
    token.setRefreshToken(UUID.randomUUID().toString());
    return repositoryToken.save(token);
  }
}
