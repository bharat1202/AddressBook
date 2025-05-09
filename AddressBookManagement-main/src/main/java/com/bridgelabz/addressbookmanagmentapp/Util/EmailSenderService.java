package com.bridgelabz.addressbookmanagmentapp.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("bharatsachdeva1012@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);
        mailSender.send(simpleMailMessage);
        System.out.println("Mail send to the user!!");
    }
}