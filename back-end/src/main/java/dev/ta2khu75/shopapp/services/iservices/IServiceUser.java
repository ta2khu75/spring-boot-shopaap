package dev.ta2khu75.shopapp.services.iservices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.ta2khu75.shopapp.dtos.DtoUser;
import dev.ta2khu75.shopapp.dtos.DtoUserUpdate;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.exceptions.InvalidParamException;
import dev.ta2khu75.shopapp.exceptions.InvalidPasswordException;
import dev.ta2khu75.shopapp.models.User;

public interface IServiceUser {
    User createUser(DtoUser dtoUser) throws DataNotFoundException, Exception;

    User login(String phoneNumber, String password) throws DataNotFoundException;

    User userUpdate(DtoUserUpdate dtoUser) throws DataNotFoundException, Exception;

    User getUserDetailFromToken(String token) throws Exception;

    User getUserDetailFromRefreshToken(String refreshToken) throws Exception;

    String loginToken(String phoneNumber, String password, int role_id)
            throws DataNotFoundException, InvalidParamException;

    Page<User> findAll(String keyword, Pageable pageable) throws Exception;

    void resetPassword(Long userId, String newPassword)throws InvalidPasswordException, DataNotFoundException;
    
    void blockOrEnable(Long userId, boolean active)throws DataNotFoundException;
}