package com.groupten.bmsproject.OTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender eMailSender;

    public void sendVerificationEmail (String toEmail, String subject, String body, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("krisdrewsimeon@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body + "Your OTP is: "+ otp);

        eMailSender.send(message);

        System.out.println("Mail sent successfully.");
    }
}
