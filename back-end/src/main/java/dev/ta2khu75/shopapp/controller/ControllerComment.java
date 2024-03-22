package dev.ta2khu75.shopapp.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import dev.ta2khu75.shopapp.dtos.DtoComment;
import dev.ta2khu75.shopapp.models.Comment;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.responses.ResponseComment;
import dev.ta2khu75.shopapp.services.ServiceComment;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/comments")

@RequiredArgsConstructor
public class ControllerComment {
  private final ServiceComment serviceComment;

  @GetMapping()
  public ResponseEntity<?> findAll(@RequestParam(value = "user_id", required = false) Long userId,
      @RequestParam("product_id") long productId) {
    try {
      // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
      // Repository Layer
      List<ResponseComment> comments;
      if (userId == null) {
        comments = serviceComment.getCommentsByProduct(productId);

      } else
        comments = serviceComment.getCommentsByUserAndProduct(userId, productId);

      return new ResponseEntity<>(comments, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> find(@PathVariable Integer id) {
    try {
      // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
      // Repository Layer
      return new ResponseEntity<>("GetOne Result", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("")
  public ResponseEntity<?> create(@Valid @RequestBody DtoComment dto) {
    try {
      User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (loginUser.getId().equals(dto.getUserId())) {
        serviceComment.insertComment(dto);
        return ResponseEntity.ok().body("Insert comment successfully");
      }
      return new ResponseEntity<>("You cannot comment as another user", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DtoComment dto) {
    try {
      User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (loginUser.getId().equals(dto.getUserId())) {
        serviceComment.updateComment(id, dto);
        return ResponseEntity.ok().body("Update comment successfully");
      }
      return new ResponseEntity<>("You cannot comment as another user", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      serviceComment.deleteComment(id);
      // TODO Implement Your Logic To Destroy Data And Return Result Through
      // ResponseEntity
      return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
