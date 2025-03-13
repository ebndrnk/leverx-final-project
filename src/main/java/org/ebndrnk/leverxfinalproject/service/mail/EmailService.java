package org.ebndrnk.leverxfinalproject.service.mail;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendConfirmationEmail(String email);

    @Async
    void sendPasswordResetEmail(String email);

    String generateVerificationCode();

    String generateResetCode();
}
