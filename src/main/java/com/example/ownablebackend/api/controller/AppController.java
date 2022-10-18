package com.example.ownablebackend.api.controller;

import com.example.ownablebackend.dto.mailservice.EmailDetails;
import com.example.ownablebackend.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

@Controller
@RestController
@RequestMapping("/api/mail")
public class AppController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @GetMapping("/contact")
    public String  showContactForm() {
        return "contact_form";
    }

    @PostMapping("/contact")
    public String submitContact(HttpServletRequest request) {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("m.mucahitbayar@gmail.com");
        message.setTo("mm1.bayar@gmail.com");

        String mailSubject = fullName + " has sent a message";
        String mailContent = "Sender Name: " + fullName + "\n";
        mailContent += "Sender E-mail: "+ email + "\n";
        mailContent += "Subject: "+ subject + "\n";
        mailContent += "Content: "+ content + "\n";

        message.setSubject(mailSubject);
        message.setText(mailContent);

        javaMailSender.send(message);
        return "message";
    }



    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }
}
