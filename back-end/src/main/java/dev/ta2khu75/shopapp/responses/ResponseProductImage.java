package dev.ta2khu75.shopapp.responses;
import dev.ta2khu75.shopapp.models.ProductImage;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseProductImage {
  private long id;
  private String image_url;
  public ResponseProductImage(ProductImage productImage){
   id=productImage.getId();
   image_url=productImage.getImageUrl(); 
  }
}
