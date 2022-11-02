package com.example.ownablebackend.api.request.pinata;

import lombok.Data;

@Data
public class PinRequest {
    PinataOptions pinataOptions;
    PinataMetaData pinataMetaData;
    PinataContent pinataContent;
}
