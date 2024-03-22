package dev.ta2khu75.shopapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ta2khu75.shopapp.models.Category;
import dev.ta2khu75.shopapp.services.iservices.IServiceCategory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("${api.prefix}/health-check")
@AllArgsConstructor
public class ControllerHealthCheck {
  private final IServiceCategory serviceCategory;

  @GetMapping()
  public ResponseEntity<?> getMethodName() {
    try {
      List<Category> list = serviceCategory.getAllCategories();
      return ResponseEntity.ok().body("ok");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("failed");
      // TODO: handle exception

    }
  }

}
