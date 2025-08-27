package com.example.emailservice.consumers;

import com.example.emailservice.dtos.EmailDto;
import com.example.emailservice.util.EmailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
@Component
public class KafkaEmailClient {
    @Autowired
    private ObjectMapper objectMapper;
    @KafkaListener(topics = "signup", groupId = "emailService")
    public void sendEmail(String message) {
        EmailDto emailDto = objectMapper.convertValue(message, EmailDto.class);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fromEmail", "password");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, "neeravpandey123@gmail.com","TLSEmail Testing Subject", "TLSEmail Testing Body");


    }
}
