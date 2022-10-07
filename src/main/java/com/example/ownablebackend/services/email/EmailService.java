package com.example.ownablebackend.services.email;

import com.example.ownablebackend.domain.User;
import com.example.ownablebackend.dto.mailservice.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails emailDetails);

    String sendMailWithAttachment(EmailDetails emailDetails);

    String sendRegisteredUserMail(EmailDetails emailDetails);

    void sendActivationMail(User user);
}
