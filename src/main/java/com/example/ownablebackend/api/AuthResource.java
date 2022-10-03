package com.example.ownablebackend.api;

import com.example.ownablebackend.api.request.LoginRequest;
import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthResource {

   UserService userService;

   public AuthResource (UserService userService) {
       this.userService = userService;
   }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
        log.info("The REST API to sign in " + loginRequest);
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        var response = userService.saveUser(signUpRequest);

        if(!Objects.isNull(response)) {
            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
