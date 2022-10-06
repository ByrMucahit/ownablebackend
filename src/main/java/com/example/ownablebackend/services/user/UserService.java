package com.example.ownablebackend.services.user;

import com.example.ownablebackend.api.request.LoginRequest;
import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.JwtResponse;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.domain.PasswordResetToken;
import com.example.ownablebackend.domain.User;
import com.example.ownablebackend.services.dto.GenericResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public interface UserService {

    MessageResponse saveUser(SignupRequest signupRequest);

    JwtResponse authenticateUser(LoginRequest loginRequest);

    GenericResponse resetPassword(String userEmail);

    void changeUserPassword(User user, String password);

    Optional<User> getUserByPasswordResetToken(final String token);
}
