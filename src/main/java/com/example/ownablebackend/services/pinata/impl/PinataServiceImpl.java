package com.example.ownablebackend.services.pinata.impl;

import com.example.ownablebackend.api.response.pinata.PinataAuthenticationResponse;
import com.example.ownablebackend.api.response.pinata.PinataPinResponse;
import com.example.ownablebackend.managers.pinata.PinataManager;
import com.example.ownablebackend.services.pinata.PinataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinataServiceImpl implements PinataService {

    private final PinataManager pinataManager;
    @Override
    public String authentication() {
        pinataManager.pinataAuthenticateApi();

        return pinataManager.pinataAuthenticateApi().getMessage();
    }

    @Override
    public PinataPinResponse pinService() {
    return this.pinataManager.pinataApiToPin();
    }
}
