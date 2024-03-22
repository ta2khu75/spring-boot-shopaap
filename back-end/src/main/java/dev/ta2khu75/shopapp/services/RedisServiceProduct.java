package dev.ta2khu75.shopapp.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ta2khu75.shopapp.responses.ResponseProductList;
import dev.ta2khu75.shopapp.services.iservices.IRedisServiceProduct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceProduct implements IRedisServiceProduct {
  private final RedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  private String getKeyFrom(String keyword, Long categoryId, PageRequest pageRequest) {
    int pageNumber = pageRequest.getPageNumber();
    int pageSize = pageRequest.getPageSize();
    Sort sort = pageRequest.getSort();
    String sortDirection=sort.getOrderFor("id").getDirection()==Sort.Direction.ASC ? "asc": "desc";
    String key=String.format("all_products:%d:%d:%s", pageNumber, pageSize, sortDirection);
    return key;
  }

  @Override
  public void clear() {
    redisTemplate.getConnectionFactory().getConnection().flushAll();
    // TODO Auto-generated method stub
  }

  @Override
  public ResponseProductList getAllProducts(String keyword, Long categoryId, PageRequest pageRequest)
      throws JsonProcessingException {
        String key=getKeyFrom(keyword, categoryId, pageRequest);
        String json=(String) redisTemplate.opsForValue().get(key);
        ResponseProductList responseProducts=json!=null?objectMapper.readValue(json, new TypeReference<ResponseProductList>() {}):null;
    // TODO Auto-generated method stub
        return responseProducts;
  }

  @Override
  public void saveAllProducts(ResponseProductList responseProductList, String keyword, Long categoryId,
      PageRequest pageRequest) throws JsonProcessingException {
    // TODO Auto-generated method stub
    String key=getKeyFrom(keyword, categoryId, pageRequest);
    String json=objectMapper.writeValueAsString(responseProductList);
    redisTemplate.opsForValue().set(key, json);
  }

}
