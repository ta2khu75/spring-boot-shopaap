package dev.ta2khu75.shopapp.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ta2khu75.shopapp.models.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseProduct extends ResponseBase{
    private Long id;
    private String name;
    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ResponseProductImage> productImages=new ArrayList<>();
    public ResponseProduct(Product product){
        id=product.getId();
        name=product.getName();
        price=product.getPrice();
        thumbnail=product.getThumbnail();
        description=product.getDescription();
        categoryId=product.getCategory().getId();
        super.setCreatedAt(product.getCreatedAt());
        super.setUpdatedAt(product.getCreatedAt());
    }
    public ResponseProduct(Product product, List<ResponseProductImage> productImages){
        id=product.getId();
        name=product.getName();
        price=product.getPrice();
        thumbnail=product.getThumbnail();
        description=product.getDescription();
        categoryId=product.getCategory().getId();
        super.setCreatedAt(product.getCreatedAt());
        super.setUpdatedAt(product.getCreatedAt());
        this.productImages=productImages;
    }
    // public static ResponseProduct formProduct(Product product){
        
    // }
}
