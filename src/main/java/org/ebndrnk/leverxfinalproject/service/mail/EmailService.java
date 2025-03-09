package org.ebndrnk.leverxfinalproject.service.mail;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.service.auth.password.ResetPasswordCodeService;
import org.ebndrnk.leverxfinalproject.service.auth.verify.VerifyEmailService;
import org.ebndrnk.leverxfinalproject.service.mail.send.EmailSender;
import org.ebndrnk.leverxfinalproject.service.mail.template.EmailTemplateGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailSender emailSender;
    private final VerifyEmailService verificationService;
    private final ResetPasswordCodeService resetPasswordCodeService;

    private final EmailTemplateGenerator emailConfirmationTemplateGenerator;

    private final EmailTemplateGenerator passwordResetTemplateGenerator;

    public EmailService(EmailSender emailSender,
                        VerifyEmailService verifyEmailService, ResetPasswordCodeService resetPasswordCodeService,
                        @Qualifier("emailConfirmationTemplateGenerator") EmailTemplateGenerator emailConfirmationTemplateGenerator,
                        @Qualifier("passwordResetTemplateGenerator") EmailTemplateGenerator passwordResetTemplateGenerator) {
        this.emailSender = emailSender;
        this.verificationService = verifyEmailService;
        this.resetPasswordCodeService = resetPasswordCodeService;
        this.emailConfirmationTemplateGenerator = emailConfirmationTemplateGenerator;
        this.passwordResetTemplateGenerator = passwordResetTemplateGenerator;
    }

    public void sendConfirmationEmail(String email) {
        String code = generateVerificationCode();
        verificationService.save(email, code);
        String content = emailConfirmationTemplateGenerator.generateEmailContent(email, code);
        emailSender.sendEmail(email, "LeverX email confirmation", content);
    }

    public void sendPasswordResetEmail(String email) {
        String code = generateResetCode();
        resetPasswordCodeService.save(email, code);
        String content = passwordResetTemplateGenerator.generateEmailContent(email, code);
        emailSender.sendEmail(email, "Reset your password", content);
    }

    /**
     * Generates a unique verification code.
     *
     * @return a unique string representing the verification code.
     */
    private String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }

    private String generateResetCode() {
        int code = new Random().nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}

