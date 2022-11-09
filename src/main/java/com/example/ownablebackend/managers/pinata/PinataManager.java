package com.example.ownablebackend.managers.pinata;

import com.example.ownablebackend.api.request.pinata.KeyValues;
import com.example.ownablebackend.api.request.pinata.PinRequest;
import com.example.ownablebackend.api.request.pinata.PinataContent;
import com.example.ownablebackend.api.request.pinata.PinataMetaData;
import com.example.ownablebackend.api.request.pinata.PinataOptions;
import com.example.ownablebackend.api.response.pinata.PinataAuthenticationResponse;
import com.example.ownablebackend.api.response.pinata.PinataPinResponse;
import com.example.ownablebackend.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PinataManager {

    private final ApplicationProperties applicationProperties;

    private final RestTemplate restTemplate;

    @Transactional
    public PinataAuthenticationResponse pinataAuthenticateApi() {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("pinata_api_key", this.applicationProperties.getPinataApiKey());
            headers.add("pinata_secret_api_key", this.applicationProperties.getPinataSecretApiKey());
            HttpEntity request = new HttpEntity(headers);

            String authenticationUrl = String.format("https://api.pinata.cloud/data/testAuthentication");

            ResponseEntity<PinataAuthenticationResponse> exchange =
                    this.restTemplate.exchange(authenticationUrl, HttpMethod.GET, request, PinataAuthenticationResponse.class);

            return exchange.getBody();

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Transactional
    public PinataPinResponse pinataApiToPin() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("pinata_api_key", this.applicationProperties.getPinataApiKey());
            headers.add("pinata_secret_api_key", this.applicationProperties.getPinataSecretApiKey());

            PinRequest pinRequest = new PinRequest();
            pinRequest.setPinataContent(new PinataContent("sels"));
            KeyValues keyValues = new KeyValues();
            keyValues.setCustomeKey("customValue");
            pinRequest.setPinataMetaData(PinataMetaData.builder().name("product").keyValues(keyValues).build());
            pinRequest.setPinataOptions(PinataOptions.builder().cidVersion(2).build());

            HttpEntity request = new HttpEntity(pinRequest,headers);
            String pinUrl = "https://api.pinata.cloud/pinning/pinJSONToIPFS";


            ResponseEntity<PinataPinResponse> exchange =
                    this.restTemplate.exchange(pinUrl, HttpMethod.POST, request, PinataPinResponse.class);

            return exchange.getBody();
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
