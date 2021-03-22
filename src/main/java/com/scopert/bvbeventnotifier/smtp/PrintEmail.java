package com.scopert.bvbeventnotifier.smtp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Profile("local")
@Component
public class PrintEmail implements EmailSender {

    @PostConstruct
    @Override
    public void isUp() {
        log.info("Email sending is disabled on local environment!");
    }

    @Override
    public void sendEmail(Message msg) throws MessagingException {
        try {
            Arrays.stream(msg.getAllRecipients())
                    .map(address -> (InternetAddress)address)
                    .forEach(address -> System.out.println("TO         : " + address.getAddress()));
            System.out.println("SUBJECT    : " + msg.getSubject());
            System.out.println("ATTACHMENT : " + ((MimeMultipart) msg.getContent()).getBodyPart(1).getFileName());
            System.out.println("BODY       : " + ((MimeMultipart) msg.getContent()).getBodyPart(0).getContent());
        } catch (IOException e) {
            log.error("Issue while accessing multipart content from email");
        }
    }

}
