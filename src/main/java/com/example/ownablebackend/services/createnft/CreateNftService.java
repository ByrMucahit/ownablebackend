package com.example.ownablebackend.services.createnft;

import com.example.ownablebackend.api.request.createnft.CreateNftRequest;
import com.example.ownablebackend.api.response.createnft.CreateNftResponse;

public interface CreateNftService {

    void makeNftForUnknownUser(CreateNftRequest createNftRequest);
}
