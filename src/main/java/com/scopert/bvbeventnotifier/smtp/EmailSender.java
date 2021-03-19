package com.scopert.bvbeventnotifier.smtp;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface EmailSender {

    void sendEmail(Message msg) throws MessagingException;
    void isUp();
}
