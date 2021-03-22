package com.scopert.bvbeventnotifier.smtp;

import com.scopert.bvbeventnotifier.attachments.TrackedEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.scopert.bvbeventnotifier.utils.DateTimeUtils.getCurrentDateInNormalFormat;

@Slf4j
@Component
public class EmailHandler {

    @Value("${mail.from}")
    private String from;

    @Value("${mail.to}")
    private String to;

    @Value("${mail.password}")
    private String password;

    @Autowired
    private Properties emailProperties;

    @Autowired
    private EmailSender emailSender;

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

    public void alertUsers(String symbol, Pair<TrackedEvents, String> event, String text, String fileName) {
        try {
            Message msg = createEmail(symbol, event, text, fileName);
            emailSender.sendEmail(msg);
        } catch (MessagingException e) {
            log.error("Could not send email notification", e);
        }
    }

    private Message createEmail(String symbol, Pair<TrackedEvents, String> event, String text, String pathToFile) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(String.format("[%s] [%s] [%s]", symbol, event.getFirst().name(), getCurrentDateInNormalFormat()));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String textWithHighlight = text.replaceAll(event.getSecond(), addColor(event.getSecond(), Color.RED));
        String textWithBreaks = textWithHighlight.replaceAll("\\n", "<br>");
        mimeBodyPart.setContent("<br>" + textWithBreaks, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        try {
            attachmentBodyPart.attachFile(new File(pathToFile));
            multipart.addBodyPart(attachmentBodyPart);
        } catch (IOException e) {
            log.error("Could not attach file to email", e);
        }

        msg.setContent(multipart);
        return msg;
    }

    public static String addColor(String msg, Color color) {
        String hexColor = String.format("#%06X",  (0xFFFFFF & color.getRGB()));
        String colorMsg = "<FONT COLOR=\"#" + hexColor + "\">" + msg + "</FONT>";
        return colorMsg;
    }

}
