package dev.ta2khu75.shopapp.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import dev.ta2khu75.shopapp.component.LocalizationUtil;
import dev.ta2khu75.shopapp.component.converter.ConverterMessageCategory;
import dev.ta2khu75.shopapp.dtos.DtoCategory;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.models.Category;
import dev.ta2khu75.shopapp.responses.ResponseObject;
import dev.ta2khu75.shopapp.services.iservices.IServiceCategory;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/categories")
// @Validated
@RequiredArgsConstructor
public class ControllerCategory {
    private final IServiceCategory serviceCategory;
    private final LocalizationUtil localizationUtil;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping()
    public ResponseEntity<?> createCategory(@Valid @RequestBody DtoCategory dtoCategory, BindingResult result) {
        if (result.hasErrors()) {
            List<String> listString = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(listString);
        }
        Category category=serviceCategory.createCategory(dtoCategory);
        kafkaTemplate.send("insert-a-category",category);
        kafkaTemplate.setMessageConverter(new ConverterMessageCategory());
        return ResponseEntity.ok(category);
    }

    @GetMapping("")
    public ResponseEntity<?> getCategory() {
        List<Category> categories=serviceCategory.getAllCategories();
        kafkaTemplate.send("get-all-categories", categories);
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findCategory(@PathVariable long id) {
        try {
            Category category = serviceCategory.getCategoryById(id);
            return ResponseEntity.ok().body(ResponseObject.builder().data(category).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseObject.builder().message(e.getMessage()).build());

            // TODO: handle exception
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCategory(@PathVariable long id, @RequestBody DtoCategory dtoCategory)
            throws DataNotFoundException {
        Category category = serviceCategory.updateCategory(id, dtoCategory);
        return ResponseEntity.ok("Update category successfully\n" + category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) {
        serviceCategory.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id:" + id + "successfully");
    }
}
