package dev.ta2khu75.shopapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.dtos.DtoProduct;
import dev.ta2khu75.shopapp.dtos.DtoProductImage;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.exceptions.InvalidParamException;
import dev.ta2khu75.shopapp.models.Category;
import dev.ta2khu75.shopapp.models.Product;
import dev.ta2khu75.shopapp.models.ProductImage;
import dev.ta2khu75.shopapp.repositories.RepositoryCategory;
import dev.ta2khu75.shopapp.repositories.RepositoryProduct;
import dev.ta2khu75.shopapp.repositories.RepositoryProductImage;
import dev.ta2khu75.shopapp.responses.ResponseProduct;
import dev.ta2khu75.shopapp.responses.ResponseProductImage;
import dev.ta2khu75.shopapp.services.iservices.IServiceProduct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceProduct implements IServiceProduct {
    private final RepositoryProduct repositoryProduct;
    private final RepositoryCategory repositoryCategory;
    private final RepositoryProductImage repositoryProductImage;
    @Override
    public Product createProduct(DtoProduct dtoProduct) throws DataNotFoundException {
        Category category=repositoryCategory.findById(dtoProduct.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find category with id:" + dtoProduct.getCategoryId()));
        Product product = Product.builder()
                .name(dtoProduct.getName())
                .price(dtoProduct.getPrice())
                .thumbnail(dtoProduct.getThumbnail())
                .description(dtoProduct.getDescription())
                .category(category)
                .build();
        // TODO Auto-generated method stub
        return repositoryProduct.save(product);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        // TODO Auto-generated method stub
        return repositoryProduct.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot found product id:"+id));
    }

    @Override
    public Page<ResponseProduct> getAllProducts(Pageable pageable, String keyword, Long category) {
        // TODO Auto-generated method stub
        return repositoryProduct.searchProducts(category, keyword, pageable).map(ResponseProduct::new);
    }

    @Override
    public Product updateProduct(long id, DtoProduct dtoProduct) throws DataNotFoundException {
        Product existingProduct=getProductById(id);
        Category category=repositoryCategory.findById(dtoProduct.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find category with id:" + dtoProduct.getCategoryId()));
        existingProduct.setPrice(dtoProduct.getPrice());
        existingProduct.setName(dtoProduct.getName());
        existingProduct.setCategory(category);
        existingProduct.setDescription(dtoProduct.getDescription());
        existingProduct.setThumbnail(dtoProduct.getThumbnail());
        // TODO Auto-generated method stub
        return repositoryProduct.save(existingProduct);
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct=repositoryProduct.findById(id);
        optionalProduct.ifPresent(repositoryProduct::delete);
    }

    @Override
    public boolean existsByName(String name) {
        // TODO Auto-generated method stub
        return repositoryProduct.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(DtoProductImage dtoProductImage) throws DataNotFoundException, InvalidParamException{
        Product product=repositoryProduct.findById(dtoProductImage.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find product with id:" + dtoProductImage.getProductId()));
        ProductImage newProductImage=ProductImage.builder()
                                                    .product(product)
                                                    .imageUrl(dtoProductImage.getImageUrl())
                                                    .build();
        //ko cho insert qua1 5 anh cho 1 sp
        int size=repositoryProductImage.findByProductId(dtoProductImage.getProductId()).size();
        if(size>=ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of image must be <="+ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return repositoryProductImage.save(newProductImage);
    }

    @Override
    public void deleteProductImage(long product_id) {
        // TODO Auto-generated method stub
        repositoryProductImage.deleteByProductId(product_id);
    }

    @Override
    public List<ResponseProductImage> getAllImage(long product_id) {
        return repositoryProductImage.findByProductId(product_id).stream().map(arg0 -> new ResponseProductImage(arg0)).toList();
        // TODO Auto-generated method stub
    }

    @Override
    public List<ResponseProduct> getProducByIds(List<Long> ids) {
        // TODO Auto-generated method stub
        return repositoryProduct.findProductByIds(ids).stream().map(arg0 -> new ResponseProduct(arg0)).toList();
    }
}
