package com.example.imageProcessing.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;


        public void sendVerificationEmail(String to , String subject , String content) throws MessagingException{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message , true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content , true);


            mailSender.send(message);
        }

}
