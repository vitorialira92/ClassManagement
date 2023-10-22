package com.liraz.classmanagement.services.emails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;


    public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // Set to true to indicate the text is HTML

        emailSender.send(message);
    }

    public void sendRegistrationEmail(String email, String name) throws MessagingException {
        //html
        String title = "Welcome to our system!";
        String message = "<b>Hello, " + name + "! It is great to see you here. </b>";
        sendHtmlMail(email, title, message);
    }
}
