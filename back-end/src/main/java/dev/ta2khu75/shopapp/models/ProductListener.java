package dev.ta2khu75.shopapp.models;

import dev.ta2khu75.shopapp.services.iservices.IRedisServiceProduct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class ProductListener {
  private final IRedisServiceProduct redisServiceProduct;
  @PrePersist
  public void prePersist(Product product){

  }

  @PostPersist
  public void postPersist(Product product){
    redisServiceProduct.clear();
  }

  @PreUpdate
  public void preUpdate(Product product){

  }

  @PostUpdate
  public void postUpdate(Product product){
    redisServiceProduct.clear();
  }

  @PreRemove
  public void preRemove(Product product){

  }

  @PostRemove
  public void postRemove(Product product){
    redisServiceProduct.clear();
  }

}
