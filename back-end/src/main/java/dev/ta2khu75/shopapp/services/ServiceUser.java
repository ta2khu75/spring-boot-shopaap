package dev.ta2khu75.shopapp.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.component.JwtTokenUtil;
import dev.ta2khu75.shopapp.dtos.DtoUser;
import dev.ta2khu75.shopapp.dtos.DtoUserUpdate;
import dev.ta2khu75.shopapp.exceptions.DataNotFoundException;
import dev.ta2khu75.shopapp.exceptions.InvalidParamException;
import dev.ta2khu75.shopapp.exceptions.InvalidPasswordException;
import dev.ta2khu75.shopapp.exceptions.PermissionDenyException;
import dev.ta2khu75.shopapp.models.Role;
import dev.ta2khu75.shopapp.models.Token;
import dev.ta2khu75.shopapp.models.User;
import dev.ta2khu75.shopapp.repositories.RepositoryRole;
import dev.ta2khu75.shopapp.repositories.RepositoryToken;
import dev.ta2khu75.shopapp.repositories.RepositoryUser;
import dev.ta2khu75.shopapp.services.iservices.IServiceUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceUser implements IServiceUser {
    private final RepositoryUser repositoryUser;
    private final RepositoryRole repositoryRole;
    private final RepositoryToken repositoryToken;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager manager;

    @Override
    public User createUser(DtoUser dtoUser) throws Exception {
        String phoneNumber = dtoUser.getPhoneNumber();
        if (repositoryUser.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number alrealy exist");
        }
        Role role = repositoryRole.findById(dtoUser.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }
        User newUser = User.builder()
                .fullName(dtoUser.getFullName())
                .phoneNumber(dtoUser.getPhoneNumber())
                .password(dtoUser.getPassword())
                .address(dtoUser.getAddress())
                .dateOfBirth(dtoUser.getDateOfBirth())
                .active(true)
                .facebookAccountId(dtoUser.getFacebookAccountId())
                .googleAccountId(dtoUser.getGoogleAccountId())
                .build();

        newUser.setRole(role);
        if (dtoUser.getFacebookAccountId() == 0 && dtoUser.getGoogleAccountId() == 0) {
            String password = dtoUser.getPassword();
            String encoderPassword = passwordEncoder.encode(password);
            newUser.setPassword(encoderPassword);
        }
        return repositoryUser.save(newUser);
    }

    @Override
    public User login(String phoneNumber, String password) throws DataNotFoundException {
        // TODO Auto-generated method stub
        User user = repositoryUser.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Sai tài khoản hoặc mật khẩu"));
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new DataNotFoundException("Sai tài khoản hoặc mật khẩu");
        }
    }

    public String loginToken(String phoneNumber, String password, int role_id)
            throws DataNotFoundException, InvalidParamException {
        // TODO Auto-generated method stub
        User user = repositoryUser.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Sai tài khoản hoặc mật khẩu"));
        if (user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong phone number or password ");
            }
        }
        Role role = repositoryRole.findById(role_id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found Role id:" + role_id));
        if (role.getId() != user.getRole().getId()) {
            throw new DataNotFoundException("Role does not existing");
        }
        if (!user.isActive()) {
            throw new DataNotFoundException("Your account locked");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, user.getAuthorities());
        manager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(user);

    }

    @Override
    public User userUpdate(DtoUserUpdate updatedDtoUser) throws Exception {
        // TODO Auto-generated method stub
        User existingUser = repositoryUser.findById(updatedDtoUser.getId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String updatedFullName = updatedDtoUser.getFullName();
        if (updatedFullName != null) {
            existingUser.setFullName(updatedFullName);
        }

        String updatedAddress = updatedDtoUser.getAddress();
        if (updatedAddress != null) {
            existingUser.setAddress(updatedAddress);
        }

        LocalDate updatedDateOfBirth = updatedDtoUser.getDateOfBirth();
        if (updatedDateOfBirth != null) {
            existingUser.setDateOfBirth(updatedDateOfBirth);
        }

        int updatedFacebookAccountId = updatedDtoUser.getFacebookAccountId();
        if (updatedFacebookAccountId > 0) {
            existingUser.setFacebookAccountId(updatedFacebookAccountId);
        }

        int updatedGoogleAccountId = updatedDtoUser.getGoogleAccountId();
        if (updatedGoogleAccountId > 0) {
            existingUser.setGoogleAccountId(updatedGoogleAccountId);
        }

        // Update password if provided
        String updatedRetypePassword = updatedDtoUser.getRetypePassword();
        String updatedPassword = updatedDtoUser.getPassword();
        if (!updatedRetypePassword.equals(updatedPassword)) {
            throw new Exception("password not math");
        }
        if (updatedPassword != null && !updatedPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedPassword));
        }
        // Update role if provided
        return repositoryUser.save(existingUser);
    }

    public User getUserDetailFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        User user = repositoryUser.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id:" + phoneNumber));
        return user;
    }

    @Override
    public User getUserDetailFromRefreshToken(String refreshToken) throws Exception {
        // TODO Auto-generated method stub
        Token existingToken = repositoryToken.findByRefreshToken(refreshToken);
        return getUserDetailFromToken(existingToken.getToken());
    }

    @Override
    public Page<User> findAll(String keyword, Pageable pageable) throws Exception{
        return repositoryUser.findAll(keyword, pageable);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) throws InvalidPasswordException, DataNotFoundException {
        // TODO Auto-generated method stub
        User existingUser=repositoryUser.findById(userId).orElseThrow(() -> new DataNotFoundException("Cannot find user with id: "+userId));
        String encodedPassword=passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);
        repositoryUser.save(existingUser);
        List<Token> tokens=repositoryToken.findByUser(existingUser);
        if(tokens==null){
            return;
        }
        for(Token token:tokens){
            repositoryToken.delete(token);
        }
    }

    @Override
    public void blockOrEnable(Long userId, boolean active) throws DataNotFoundException {
        // TODO Auto-generated method stub
        User existingUser=repositoryUser.findById(userId).orElseThrow(() -> new DataNotFoundException("Cannot find user by id: "+userId));
        existingUser.setActive(active);
        repositoryUser.save(existingUser);
    }
}
