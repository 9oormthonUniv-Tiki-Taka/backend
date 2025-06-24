package com.tikitaka.api.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
