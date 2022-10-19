package com.example.ownablebackend.api.response.createnft;

import lombok.Data;

@Data
public class CreateNftResponse {
    private boolean error;

    private String message;

    private String key;
}
