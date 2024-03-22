package dev.ta2khu75.shopapp.repositories;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Product;

@Repository
public interface RepositoryProduct extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);

    @Query("Select p From Product p where (:category is null or :category=0 or p.category.id=:category) and (:keyword is null or :keyword='' or p.name like %:keyword%)")
    Page<Product> searchProducts(@Param("category") long category, @Param("keyword") String keyword, Pageable page);

    @Query("Select p From Product p where p.id in :ids")
    List<Product> findProductByIds(@Param("ids") List<Long> ids);
}
