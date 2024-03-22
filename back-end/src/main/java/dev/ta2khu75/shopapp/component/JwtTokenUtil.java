package dev.ta2khu75.shopapp.component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dev.ta2khu75.shopapp.exceptions.InvalidParamException;
import dev.ta2khu75.shopapp.models.Token;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.repositories.RepositoryToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
  @Value("${jwt.expiration}")
  private int expiration;
  @Value("${jwt.secretKey}")
  private String secretKey;
  private final RepositoryToken repositoryToken;
  //Hàm tạo token
  public String generateToken(User user) throws InvalidParamException{
    //Tạo claims dể lưu vào token
    Map<String, Object> claims=new HashMap<>();
    claims.put("phoneNumber", user.getPhoneNumber());
    claims.put("userId",user.getId());
    try {
      String token=Jwts.builder()//.claims().empty().add(claims).add("null", claims)
                        .setClaims(claims)// đưa vào token sẽ có lúc phai3 lấy ra
                        .setSubject(user.getPhoneNumber())
                        .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                        .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                        .compact();
      return token;
    } catch (Exception e) {

      throw new InvalidParamException("Cannot create jwt token, error:"+e.getMessage());
      // TODO: handle exception
    }
  }
  //Hàm tạo key
  private Key getSignInKey(){
    byte[] bytes=Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(bytes);
  }
  private String generateSecretKey(){
    SecureRandom random=new SecureRandom();
    byte[] keyBytes=new byte[32];
    random.nextBytes(keyBytes);
    String secretKey=Encoders.BASE64.encode(keyBytes);
    return secretKey;
  }
  //Đây là hàm dựa vào token để lấy claims ra
  private Claims extractAllClaims(String token){
    return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
  }
  public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
    final Claims claims=extractAllClaims(token);
    return claimsResolver.apply(claims);
  }
  
  public boolean isTokenExpired(String token){
    Date expirationDate=this.extractClaim(token, Claims::getExpiration);
    return expirationDate.before(new Date());
  }
  public String extractPhoneNumber(String token){
    return extractClaim(token, Claims::getSubject);
  }
  public boolean validateToken(String token, User user){
    String phoneNumber=extractPhoneNumber(token);
    Token existingToken=repositoryToken.findByToken(token);
    if(existingToken==null || existingToken.isRevoked()==true || !user.isActive())return false;
    return (phoneNumber.equals(user.getUsername())) && !isTokenExpired(token);
  }
}
