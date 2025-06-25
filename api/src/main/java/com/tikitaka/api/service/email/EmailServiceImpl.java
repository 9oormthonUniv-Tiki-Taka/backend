package com.tikitaka.api.service.email;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noti@taehyeok.org");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패: " + e.getMessage());
        }
    }
}
