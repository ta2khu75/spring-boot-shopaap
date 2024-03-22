package dev.ta2khu75.shopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.ta2khu75.shopapp.responses.ResponseObject;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GlobalExceptionHandle {
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ResponseObject> handleGeneralException(Exception e) {
    return ResponseEntity.internalServerError().body(
        ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage()).data(e).build());
  }
  @ExceptionHandler(DataNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ResponseObject> handleGeneralException(DataNotFoundException e) {
    return ResponseEntity.internalServerError().body(
        ResponseObject.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage()).data(e).build());
  }
}
