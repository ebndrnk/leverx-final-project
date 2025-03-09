package org.ebndrnk.leverxfinalproject.service.mail.send;

public interface EmailSender {
    void sendEmail(String email, String subject, String content);
}

