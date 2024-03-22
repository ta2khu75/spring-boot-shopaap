package dev.ta2khu75.shopapp.component;

import java.util.List;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ta2khu75.shopapp.models.Category;

@Component
@KafkaListener(id="groupA", topics = {"get-all-categories", "insert-a-category"})
public class MyKafkaListener {
  @KafkaHandler
  public void listenCategory(Category category){
    System.out.println("Received: "+category);
  }
  @KafkaHandler(isDefault = true)
  public void unknown(Object object){
    System.out.println("Received unknown: "+object);
  }
  @KafkaHandler
  public void listenCategories(List<Category> categories){
    System.out.println("Received: "+categories);
  }
}