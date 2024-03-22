package dev.ta2khu75.shopapp.services.iservices;

import java.util.List;

import dev.ta2khu75.shopapp.dtos.DtoComment;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Comment;
import dev.ta2khu75.shopapp.responses.ResponseComment;

public interface IServiceComment {

  ResponseComment insertComment(DtoComment comment) throws DataNotFoundException;

  void updateComment(Long commentId, DtoComment comment) throws DataNotFoundException;

  void deleteComment(Long commentId);

  List<ResponseComment> getCommentsByUser(Long userId);

  List<ResponseComment> getCommentsByUserAndProduct(Long userId, Long productId);

  List<ResponseComment> getCommentsByProduct(Long productId);
}
