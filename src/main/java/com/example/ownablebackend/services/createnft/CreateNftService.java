package com.example.ownablebackend.services.createnft;

import com.example.ownablebackend.api.request.createnft.CreateNftRequest;

public interface CreateNftService {

    void makeNftForUnknownUser(CreateNftRequest createNftRequest);
}
