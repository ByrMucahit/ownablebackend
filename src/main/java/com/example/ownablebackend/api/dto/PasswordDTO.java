package com.example.ownablebackend.api.dto;

import lombok.Data;

@Data
public class PasswordDTO {

    private String oldPassword;

    private  String token;

    private String newPassword;
}
