package dev.ta2khu75.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.dtos.DtoComment;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Comment;
import dev.ta2khu75.shopapp.models.Product;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.repositories.RepositoryComment;
import dev.ta2khu75.shopapp.repositories.RepositoryProduct;
import dev.ta2khu75.shopapp.repositories.RepositoryUser;
import dev.ta2khu75.shopapp.responses.ResponseComment;
import dev.ta2khu75.shopapp.services.iservices.IServiceComment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceComment implements IServiceComment {
    private final RepositoryComment repositoryComment;
    private final RepositoryUser repositoryUser;
    private final RepositoryProduct repositoryProduct;
    @Override
    public ResponseComment insertComment(DtoComment comment) throws DataNotFoundException {
      User user=repositoryUser.findById(comment.getUserId()).orElseThrow(() ->  new DataNotFoundException("Could not find user by id:"+comment.getUserId()));
      Product product=repositoryProduct.findById(comment.getProductId()).orElseThrow(() ->  new DataNotFoundException("Could not find product by id:"+comment.getProductId()));
      Comment newComment=Comment.builder().content(comment.getContent()).user(user).product(product).build();
      newComment=repositoryComment.save(newComment);
      return new  ResponseComment(newComment);
      // TODO Auto-generated method stub
    }

    @Override
    public void updateComment(Long commentId, DtoComment comment) throws DataNotFoundException {
      Comment existingComment=repositoryComment.findById(commentId).orElseThrow(() -> new DataNotFoundException("Couldn't find comment with id: "+commentId));
      // TODO Auto-generated method stub
      existingComment.setContent(comment.getContent());
      repositoryComment.save(existingComment);
    }

    @Override
    public void deleteComment(Long commentId) {
      repositoryComment.deleteById(commentId);
    }

    @Override
    public List<ResponseComment> getCommentsByUser(Long userId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'getCommentsByUser'");
    }

    @Override
    public List<ResponseComment> getCommentsByProduct(Long productId) {
      return repositoryComment.findByProductId(productId).stream().map(t -> new ResponseComment(t)).toList();

    }

    @Override
    public List<ResponseComment> getCommentsByUserAndProduct(Long userId, Long productId) {
      // TODO Auto-generated method stub
      return repositoryComment.findByUserIdAndProductId(userId, productId).stream().map(t -> new ResponseComment(t)).toList();
    }
}
