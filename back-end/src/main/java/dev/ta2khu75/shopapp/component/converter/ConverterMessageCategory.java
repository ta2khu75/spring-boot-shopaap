package dev.ta2khu75.shopapp.component.converter;

import java.util.Collections;

import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.stereotype.Component;

import dev.ta2khu75.shopapp.models.Category;

@Component
public class ConverterMessageCategory extends JsonMessageConverter {
  public ConverterMessageCategory(){
    super();
    DefaultJackson2JavaTypeMapper typeMapper=new DefaultJackson2JavaTypeMapper();
    typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
    typeMapper.addTrustedPackages("dev.ta2khu75.shopapp");
    typeMapper.setIdClassMapping(Collections.singletonMap("category", Category.class));
    this.setTypeMapper(typeMapper);
  }
}
