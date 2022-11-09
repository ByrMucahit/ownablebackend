package com.example.ownablebackend.api.controller.pinata;

import com.example.ownablebackend.api.response.pinata.PinataAuthenticationResponse;
import com.example.ownablebackend.api.response.pinata.PinataPinResponse;
import com.example.ownablebackend.services.pinata.PinataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pinata")
@RequiredArgsConstructor
public class PinataController {

    private final PinataService pinataService;

    @GetMapping("/create-auth-key")
    public String generateAuthKey() {
        return this.pinataService.authentication();
    }

    @PostMapping("/pin-to-ipfs")
    public PinataPinResponse pinToIpfs() {
        return this.pinataService.pinService();
    }
}
