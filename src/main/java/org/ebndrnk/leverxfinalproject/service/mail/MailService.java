package org.ebndrnk.leverxfinalproject.service.mail;

import jakarta.mail.internet.MimeMessage;

public interface MailService {
    void send(String userEmail);
    MimeMessage createMessage(String userEmail, String verifyCode);
}
