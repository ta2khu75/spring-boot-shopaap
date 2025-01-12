package dev.ta2khu75.shopapp.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.ta2khu75.shopapp.services.iservices.IServiceRole;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class ControllerRole {
    private final IServiceRole serviceRole;
    @GetMapping()
    public ResponseEntity<?> findAll() {
        try {
            
            //TODO Implement Your Logic To Get Data From Service Layer Or Directly From Repository Layer
            return new ResponseEntity<>(serviceRole.getAllRole(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<?> find(@PathVariable Integer id) {
    //     try {
    //         //TODO Implement Your Logic To Get Data From Service Layer Or Directly From Repository Layer
    //         return new ResponseEntity<>("GetOne Result", HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

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

    // @DeleteMapping("/{id}")
    // public ResponseEntity<?> delete(@PathVariable Integer id) {
    //     try {
    //         //TODO Implement Your Logic To Destroy Data And Return Result Through ResponseEntity
    //         return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
}
