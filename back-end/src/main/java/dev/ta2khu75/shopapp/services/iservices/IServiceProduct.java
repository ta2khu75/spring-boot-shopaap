package dev.ta2khu75.shopapp.services.iservices;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.ta2khu75.shopapp.dtos.DtoProduct;
import dev.ta2khu75.shopapp.dtos.DtoProductImage;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.exceptions.InvalidParamException;
import dev.ta2khu75.shopapp.models.Product;
import dev.ta2khu75.shopapp.models.ProductImage;
import dev.ta2khu75.shopapp.responses.ResponseProduct;
import dev.ta2khu75.shopapp.responses.ResponseProductImage;


public interface IServiceProduct {
    Product createProduct(DtoProduct dtoProduct) throws DataNotFoundException;
    Product getProductById(long id) throws DataNotFoundException;
    Page<ResponseProduct> getAllProducts(Pageable pageable, String keyword, Long cagetory);
    Product updateProduct(long id, DtoProduct dtoProduct) throws DataNotFoundException;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(DtoProductImage dtoProductImage) throws DataNotFoundException, InvalidParamException;
    void deleteProductImage(long product_id);
    List<ResponseProductImage> getAllImage(long product_id);
    List<ResponseProduct> getProducByIds(List<Long> arrayId);

}