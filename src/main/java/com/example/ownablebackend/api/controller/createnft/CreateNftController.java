package com.example.ownablebackend.api.controller.createnft;

import com.example.ownablebackend.api.request.createnft.CreateNftRequest;
import com.example.ownablebackend.services.createnft.CreateNftService;
import com.example.ownablebackend.services.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class CreateNftController {

    private final CreateNftService createNftService;

    @PostMapping(value = "/create/unknownuser" , consumes = MediaType.ALL_VALUE, produces = {MediaType.ALL_VALUE, "application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createNftForUnknownUser( @ModelAttribute @Valid CreateNftRequest createNftRequest) {
        log.info("NFT MAKING initialize for {}", createNftRequest.getEmail());
        this.createNftService.makeNftForUnknownUser(createNftRequest);
    }
}
