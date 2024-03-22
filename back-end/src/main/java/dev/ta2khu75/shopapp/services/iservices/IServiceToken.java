package dev.ta2khu75.shopapp.services.iservices;

import dev.ta2khu75.shopapp.models.Token;
import dev.ta2khu75.shopapp.models.User;

public interface IServiceToken {
  Token addToken(User user, String token, boolean isMobile);
  Token refreshToken(String refreshToken, User user) throws Exception;
}
