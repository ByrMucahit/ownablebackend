package com.example.ownablebackend.services.createnft.impl;

import com.example.ownablebackend.api.request.SignupRequest;
import com.example.ownablebackend.api.request.createnft.CreateNftRequest;
import com.example.ownablebackend.api.response.MessageResponse;
import com.example.ownablebackend.api.response.createnft.CreateNftResponse;
import com.example.ownablebackend.client.ipfs.IpfsClient;
import com.example.ownablebackend.domain.Files;
import com.example.ownablebackend.services.UserService;
import com.example.ownablebackend.services.createnft.CreateNftService;
import com.example.ownablebackend.services.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateNftServiceImpl implements CreateNftService {

    private final FileService fileService;

    private final UserService userService;

    private final IpfsClient ipfsClient;


    @Override
    public void makeNftForUnknownUser(CreateNftRequest createNftRequest) {
        // First Registered
        log.info("Checks user {} registered", createNftRequest.getEmail());
       this.checkUserRegistered(userService.saveUser(prepareSignupRequest(createNftRequest)));

        // Second Adds file to ipfs
        log.info("SAVE FILE INTO IPFS");
        String hash = ipfsClient.saveFile(createNftRequest.getImage());
        log.info("FILE SAVED, {}", hash.isEmpty());

        // Third insert into my db
        log.info("CREATE FILE INTO OWNABLE-NS");
        fileService.saveFile(prepareFiles(createNftRequest, hash));
        log.info("TRANSACTION HAS BEEN DONE SUCCESFULLY");
    }

    private Files prepareFiles(CreateNftRequest createNftRequest, String hash) {
        return Files.builder()
                .hash(hash)
                .description(createNftRequest.getDescription())
                .tokenName(createNftRequest.getTokenName())
                .typeOfAsset(createNftRequest.getTypeOfAsset())
                .tokenName(createNftRequest.getTokenName())
                .build();
    }


    private SignupRequest prepareSignupRequest(CreateNftRequest createNftRequest) {
        return  SignupRequest.builder()
                .firstName(createNftRequest.getFirstName())
                .lastName(createNftRequest.getLastName())
                .email(createNftRequest.getEmail())
                .password(createNftRequest.getPassword())
                .confirmPassword(createNftRequest.getConfirmPassword()).build();
    }

    private void checkUserRegistered(MessageResponse response) {
        if(Objects.isNull(response)) {
           throw new RuntimeException("User haven't been registered ");
        }
        log.info("User has been registered.");
    }
}
