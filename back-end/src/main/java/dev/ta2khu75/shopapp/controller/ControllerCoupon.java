package dev.ta2khu75.shopapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.ta2khu75.shopapp.responses.ResponseCouponCalculate;
import dev.ta2khu75.shopapp.services.iservices.IServiceCoupon;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class ControllerCoupon {
  private final IServiceCoupon serviceCoupon;
    @GetMapping("/calculate")
    public ResponseEntity<ResponseCouponCalculate> calculate(@RequestParam String couponCode, @RequestParam double totalAmount) {
        try {
          double finalAmount=serviceCoupon.calculateCouponValue(couponCode, totalAmount);
            //TODO Implement Your Logic To Get Data From Service Layer Or Directly From Repository Layer
            return new ResponseEntity<>(ResponseCouponCalculate.builder().result(finalAmount).message("Đã áp dụng mã").build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseCouponCalculate.builder().result(totalAmount).message(e.getMessage()).build(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Integer id) {
        try {
            //TODO Implement Your Logic To Get Data From Service Layer Or Directly From Repository Layer
            return new ResponseEntity<>("GetOne Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping()
    // public ResponseEntity<?> create(@RequestBody Dto dto) {
    //     try {
    //         //TODO Implement Your Logic To Save Data And Return Result Through ResponseEntity
    //         return new ResponseEntity<>("Create Result", HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @PutMapping()
    // public ResponseEntity<?> update(@RequestBody Dto dto) {
    //     try {
    //         //TODO Implement Your Logic To Update Data And Return Result Through ResponseEntity
    //         return new ResponseEntity<>("Update Result", HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            //TODO Implement Your Logic To Destroy Data And Return Result Through ResponseEntity
            return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
