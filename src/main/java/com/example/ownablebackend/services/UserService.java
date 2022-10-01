package com.example.ownablebackend.services;

import com.example.ownablebackend.api.request.LoginRequest;
import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.response.JwtResponse;
import com.example.ownablebackend.api.response.MessageResponse;


public interface UserService {

    MessageResponse saveUser(SignupRequest signupRequest);

    JwtResponse authenticateUser(LoginRequest loginRequest);
}
