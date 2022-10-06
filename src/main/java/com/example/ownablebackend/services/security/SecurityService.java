package com.example.ownablebackend.services.security;

public interface SecurityService {
    String validatePasswordResetToken(String token);
}
