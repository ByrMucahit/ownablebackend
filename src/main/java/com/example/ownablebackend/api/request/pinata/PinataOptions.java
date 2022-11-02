package com.example.ownablebackend.api.request.pinata;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PinataOptions {
    private int cidVersion;
}
