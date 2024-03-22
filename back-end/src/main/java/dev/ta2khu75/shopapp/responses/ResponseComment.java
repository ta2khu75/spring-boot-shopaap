package dev.ta2khu75.shopapp.responses;

import dev.ta2khu75.shopapp.models.Comment;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseComment {
  private String content;
  private ResponseUser user;
  public ResponseComment(Comment comment){
    content=comment.getContent();
    user=new ResponseUser(comment.getUser());
  }
}
