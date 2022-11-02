package com.example.ownablebackend.api.request.pinata;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PinataMetaData {
    private String name;
    private KeyValues keyValues;

}
