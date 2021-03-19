package com.scopert.bvbeventnotifier.smtp;

import com.scopert.bvbeventnotifier.scheduler.LocalEnvironmentTestingTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

@Slf4j
@Component
@ConditionalOnMissingBean(LocalEnvironmentTestingTask.class)
public class SendEmail implements EmailSender {

    @PostConstruct
    @Override
    public void isUp() {
        log.info("Email sending is enabled!");
    }

    public void sendEmail(Message msg) throws MessagingException {
        Transport.send(msg);
    }

}
