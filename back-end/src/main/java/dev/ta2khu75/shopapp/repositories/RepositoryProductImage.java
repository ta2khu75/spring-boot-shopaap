package dev.ta2khu75.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.ProductImage;

@Repository
public interface RepositoryProductImage extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(long product_id);
    void deleteByProductId(long product_id);
}
