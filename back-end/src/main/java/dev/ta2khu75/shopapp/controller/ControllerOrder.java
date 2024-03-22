package dev.ta2khu75.shopapp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import dev.ta2khu75.shopapp.component.LocalizationUtil;
import dev.ta2khu75.shopapp.dtos.DtoOrder;
import dev.ta2khu75.shopapp.models.Order;
import dev.ta2khu75.shopapp.responses.ResponseOrder;
import dev.ta2khu75.shopapp.responses.ResponseOrderList;
import dev.ta2khu75.shopapp.services.iservices.IServiceOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class ControllerOrder {
    private final IServiceOrder serviceOrder;
    private final LocalizationUtil localizationUtil;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findAll(@PathVariable Long id) {
        try {
            // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
            // Repository Layer
            List<ResponseOrder> listOrder = serviceOrder.getAllOrdersByUserId(id);
            return new ResponseEntity<>(listOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<?> find(@PathVariable("order-id") Long id) {
        try {
            // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
            // Repository Layer
            ResponseOrder existingOrder = serviceOrder.getOrderById(id);
            return new ResponseEntity<>(existingOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody DtoOrder dtoOrder, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
            // TODO Implement Your Logic To Save Data And Return Result Through
            // ResponseEntity
            return new ResponseEntity<>(serviceOrder.createOrder(dtoOrder), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DtoOrder dtoOrder) {
        try {
            return new ResponseEntity<>(serviceOrder.updateOrder(id, dtoOrder), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            // TODO Implement Your Logic To Destroy Data And Return Result Through
            // ResponseEntity
            serviceOrder.deleteOrder(id);
            return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseOrderList> getOrdersByKeyword(@RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int limit) {
        System.out.println(String.format("%s page%d   limit%d", keyword, page, limit));
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<Order> pageOrders = serviceOrder.getAllOrderByKeyword(keyword, pageRequest);

        return ResponseEntity.ok()
                .body(ResponseOrderList.builder()
                        .orders(pageOrders.stream().map(arg0 -> new ResponseOrder(arg0)).toList())
                        .totalPages(pageOrders.getTotalPages()).build());
    }

}
