package dev.ta2khu75.shopapp.controller;

import java.util.List;
import java.util.UUID;

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
import dev.ta2khu75.shopapp.dtos.DtoLogin;
import dev.ta2khu75.shopapp.dtos.DtoRefreshToken;
import dev.ta2khu75.shopapp.dtos.DtoUser;
import dev.ta2khu75.shopapp.dtos.DtoUserUpdate;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.exceptions.InvalidPasswordException;
import dev.ta2khu75.shopapp.models.Token;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.responses.ResponseLogin;
import dev.ta2khu75.shopapp.responses.ResponseRegister;
import dev.ta2khu75.shopapp.responses.ResponseUser;
import dev.ta2khu75.shopapp.responses.ResponseUserList;
import dev.ta2khu75.shopapp.services.iservices.IServiceToken;
import dev.ta2khu75.shopapp.services.iservices.IServiceUser;
import dev.ta2khu75.shopapp.utils.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class ControllerUser {
    private final IServiceUser serviceUser;
    private final IServiceToken serviceToken;
    private final LocalizationUtil localizationUtil;

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int limit, @RequestParam(defaultValue = "") String keyword) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
            // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
            // Repository Layer
            Page<User> pageUsers = serviceUser.findAll(keyword, pageRequest);
            List<ResponseUser> listResponseUsers = pageUsers.getContent().stream().map(t -> new ResponseUser(t))
                    .toList();
            return new ResponseEntity<>(
                    ResponseUserList.builder().users(listResponseUsers).totalPages(pageUsers.getTotalPages()).build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user-detail")
    public ResponseEntity<?> update(@RequestBody DtoUserUpdate dto,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // TODO Implement Your Logic To Update Data And Return Result Through
            // ResponseEntity
            String extractedToken = authorizationHeader.substring(7);
            User user = serviceUser.getUserDetailFromToken(extractedToken);
            if (user.getId() != dto.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return new ResponseEntity<>(new ResponseUser(serviceUser.userUpdate(dto)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        // TODO: process POST request
        try {
            String extractedToken = authorizationHeader.substring(7);
            User user = serviceUser.getUserDetailFromToken(extractedToken);
            return new ResponseEntity<>(new ResponseUser(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            // TODO: handle exception
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseRegister> create(@Valid @RequestBody DtoUser dtoUser, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> fieldError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                StringBuilder string = new StringBuilder();
                fieldError.forEach(arg0 -> string.append(arg0));
                return new ResponseEntity<>(ResponseRegister.builder().message(string.toString()).build(),
                        HttpStatus.BAD_REQUEST);
            }
            if (!dtoUser.getPassword().equals(dtoUser.getRetypePassword())) {
                return ResponseEntity.badRequest().body(ResponseRegister.builder()
                        .message(localizationUtil.getLocalizedMessage(Message.PASSWORD_NOT_MATCH)).build());
            }
            User user = serviceUser.createUser(dtoUser);
            // TODO Implement Your Logic To Save Data And Return Result Through
            // ResponseEntity
            return ResponseEntity.ok().body(ResponseRegister.builder()
                    .message(localizationUtil.getLocalizedMessage(Message.REGISTER_SUCCESSFULLY)).user(user).build());// "Create
                                                                                                                      // Result
                                                                                                                      // "+
                                                                                                                      // ,
                                                                                                                      // HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseRegister.builder()
                    .message(localizationUtil.getLocalizedMessage(Message.REGISTER_FAILED, e.getMessage())).build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> read(@Valid @RequestBody DtoLogin dtoLogin, BindingResult result,
            HttpServletRequest request) {
        try {
            // TODO Implement Your Logic To Save Data And Return Result Through
            // ResponseEntity
            if (result.hasErrors()) {
                List<String> fieldError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return new ResponseEntity<>(fieldError,
                        HttpStatus.BAD_REQUEST);
            }
            String token = serviceUser.loginToken(dtoLogin.getPhoneNumber(), dtoLogin.getPassword(),
                    dtoLogin.getRoleId() == null ? 1 : dtoLogin.getRoleId());
            String userAgent = request.getHeader("User-Agent");
            User user = serviceUser.getUserDetailFromToken(token);
            Token refreshToken = serviceToken.addToken(user, token, isMobileDevice(userAgent));
            String message = localizationUtil.getLocalizedMessage(Message.LOGIN_SUCCESSFULLY);
            return ResponseEntity
                    .ok(ResponseLogin.builder().message(message).token(token)
                            .refreshToken(refreshToken.getRefreshToken()).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseLogin.builder()
                    .message(localizationUtil.getLocalizedMessage(Message.LOGIN_FAILED, e.getMessage())).build());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> postMethodName(@Valid @RequestBody DtoRefreshToken dtoRefreshToken) {
        // TODO: process POST request
        try {
            User userDetail = serviceUser.getUserDetailFromRefreshToken(dtoRefreshToken.getRefreshToken());
            Token token = serviceToken.refreshToken(dtoRefreshToken.getRefreshToken(), userDetail);
            String message = "Refresh token successfully";
            return ResponseEntity
                    .ok(ResponseLogin.builder().message(message).token(token.getToken())
                            .refreshToken(token.getRefreshToken()).build());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PutMapping("/reset-password/{userId}")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resetPassword(@Valid @PathVariable long userId) {
        try {
            // TODO Implement Your Logic To Update Data And Return Result Through
            // ResponseEntity
            String newPassword = UUID.randomUUID().toString().substring(0, 5);
            serviceUser.resetPassword(userId, newPassword);
            return new ResponseEntity<>(newPassword, HttpStatus.OK);
        } catch (InvalidPasswordException e) {
            return new ResponseEntity<>("Invalid password", HttpStatus.BAD_REQUEST);

        } catch (DataNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/block-or-enable/{userId}/{active}")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> blockOrEnable(@Valid @PathVariable long userId, @Valid @PathVariable int active) {
        try {
            // TODO Implement Your Logic To Update Data And Return Result Through
            // ResponseEntity
            serviceUser.blockOrEnable(userId, active > 0);
            String message=active>0?"Successfully enable the user id %s.":"Successfully blocker the user id %s.";
            return new ResponseEntity<>(String.format(message, userId), HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            // TODO Implement Your Logic To Destroy Data And Return Result Through
            // ResponseEntity
            return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isMobileDevice(@NotNull String agent) {
        return agent.toLowerCase().equals("mobile");
    }
}
