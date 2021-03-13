package com.scopert.bvbeventnotifier.smtp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
public class EmailSender {

    @Value("${mail.from}")
    private String from;

    @Value("${mail.to}")
    private String to;

    @Value("${mail.password}")
    private String password;

    @Autowired
    private Properties emailProperties;

    @PostConstruct
    public void initEmailSession() {
        session = Session.getInstance(emailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

    private Session session;

    //TODO Maybe a cache or something else should be used to check if we have duplicated email from some reason
    //TODO de ce nu as avea pe viitor si PDF ul ? o avea impact asupra timpului de trimitere ? ...
    //TODO trebuie facute niste profile, sa nu mai comentez mereu SEND ul si apoi sa l uit asa
    public void sendEmail(String symbol, String trackedPhrase, String fileName) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(String.format("Eveniment important pentru: %s", symbol));
            msg.setText(String.format("Fraza cheie gasita: [%s] in documentul [%s]", trackedPhrase, fileName));
            Transport.send(msg);
        } catch (MessagingException e) {
            log.error("Could not send email notification", e);
        }
    }

}
