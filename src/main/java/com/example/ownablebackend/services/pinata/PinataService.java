package com.example.ownablebackend.services.pinata;

import com.example.ownablebackend.api.response.pinata.PinataAuthenticationResponse;

public interface PinataService {

    String authentication();

    PinataAuthenticationResponse pinService();
}
