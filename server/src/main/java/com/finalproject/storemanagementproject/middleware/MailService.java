package com.finalproject.storemanagementproject.middleware;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.host}")
    public String DOMAIN;

    @Value("${spring.mail.username}")
    private String USERNAME;

    @Value("${spring.mail.password}")
    private String PASSWORD;

    @Value("${spring.mail.port}")
    private int PORT;

    public boolean sendResetPasswordMail(String email, String token) {
        String subject = "Reset password";
        String content = "Click this link to reset your password: " + DOMAIN + "/reset-password/" + token;
        return sendMail(email, subject, content);
    }

    public boolean sendMail(String email, String subject, String content) {

        return true;
    }

}
