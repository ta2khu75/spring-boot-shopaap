package dev.ta2khu75.shopapp.services.iservices;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.ta2khu75.shopapp.responses.ResponseProduct;
import dev.ta2khu75.shopapp.responses.ResponseProductList;

public interface IRedisServiceProduct {
  void clear(); //clear catch

  ResponseProductList getAllProducts(String keyword, Long categoryId, PageRequest pageRequest)
      throws JsonProcessingException;

  void saveAllProducts(ResponseProductList responseProductList, String keyword, Long categoryId, PageRequest pageRequest)
      throws JsonProcessingException;
}
