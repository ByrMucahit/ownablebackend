package com.example.ownablebackend.services.pinata;

import com.example.ownablebackend.api.response.pinata.PinataPinResponse;

public interface PinataService {

    String authentication();

    PinataPinResponse pinService();
}
