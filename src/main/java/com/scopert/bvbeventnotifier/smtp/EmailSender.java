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

    @Value("${mail.enabled}")
    private boolean enabled;

    @Autowired
    private Properties emailProperties;

    private Session session;

    @PostConstruct
    public void initEmailSession() {
        session = Session.getInstance(emailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

    public void alertUsers(String symbol, String trackedPhrase, String fileName) {
        try {
            Message msg = createEmail(symbol, trackedPhrase, fileName);
            sendEmail(msg);
        } catch (MessagingException e) {
            log.error("Could not send email notification", e);
        }
    }

    private Message createEmail(String symbol, String trackedPhrase, String fileName) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(String.format("Eveniment important pentru: %s", symbol));
        msg.setText(String.format("Fraza cheie gasita: [%s] in documentul [%s]", trackedPhrase, fileName));
        return msg;
    }

    private void sendEmail(Message msg) throws MessagingException {
        if(enabled){
            Transport.send(msg);
        } else {
            log.info("Email sending is disabled on local environment!");
        }
    }

}
