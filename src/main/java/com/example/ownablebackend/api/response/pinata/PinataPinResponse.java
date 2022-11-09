package com.example.ownablebackend.api.response.pinata;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PinataPinResponse {
    private String ipfsHash;
    private int pinSize;
    private Timestamp timestamp;
    private boolean isDuplicate;
}
