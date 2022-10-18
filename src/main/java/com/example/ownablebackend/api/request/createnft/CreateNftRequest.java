package com.example.ownablebackend.api.request.createnft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNftRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private String phoneNumber;

    private String password;

    private String confirmPassword;

    private String tokenName;

    private String typeOfAsset;

    private String description;

    private Integer nftMiningFee;

    private MultipartFile image;
}
