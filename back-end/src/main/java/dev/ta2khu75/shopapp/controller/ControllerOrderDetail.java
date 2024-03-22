package dev.ta2khu75.shopapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import dev.ta2khu75.shopapp.component.LocalizationUtil;
import dev.ta2khu75.shopapp.dtos.DtoOrderDetail;
import dev.ta2khu75.shopapp.responses.ResponseOrderDetail;
import dev.ta2khu75.shopapp.services.iservices.IServiceOrderDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class ControllerOrderDetail {
    private final IServiceOrderDetail serviceOrderDetail;    
    private final LocalizationUtil localizSationUtil;

    @GetMapping("/order/{id}")
    public ResponseEntity<?> findAll(@PathVariable Long id) {
        try {
            List<ResponseOrderDetail> listResponseOrderDetails=serviceOrderDetail.getAllOrders(id);
            //TODO Implement Your Logic To Get Data From Service Layer Or Directly From Repository Layer
            return new ResponseEntity<>(listResponseOrderDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        try {
            //TODO Implement Your Logic To Get Data From Service Layer Or Directly From Repository Layer
            return new ResponseEntity<>(serviceOrderDetail.getOrderDetailById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody DtoOrderDetail dto, BindingResult result) {
        try {
            //TODO Implement Your Logic To Save Data And Return Result Through ResponseEntity
            if(result.hasErrors()){
                List<String> error=result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            ResponseOrderDetail responseOrderDetail=serviceOrderDetail.createOrderDetail(dto);
            return new ResponseEntity<>(responseOrderDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DtoOrderDetail dto) {
        try {
            ResponseOrderDetail responseOrderDetail=serviceOrderDetail.updateOrderDetail(id, dto);
            //TODO Implement Your Logic To Update Data And Return Result Through ResponseEntity
            return new ResponseEntity<>(responseOrderDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            serviceOrderDetail.deleteOrderDetail(id);
            //TODO Implement Your Logic To Destroy Data And Return Result Through ResponseEntity
            return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
