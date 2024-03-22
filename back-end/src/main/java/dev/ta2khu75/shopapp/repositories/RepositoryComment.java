package dev.ta2khu75.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Comment;
import dev.ta2khu75.shopapp.models.Product;

@Repository
public interface RepositoryComment extends JpaRepository<Comment, Long> {
  List<Comment>findByUserIdAndProductId(Long userId, Long productId);
  List<Comment> findByProductId(Long product);
}
