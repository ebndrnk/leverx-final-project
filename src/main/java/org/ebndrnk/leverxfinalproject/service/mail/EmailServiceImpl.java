package org.ebndrnk.leverxfinalproject.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.service.auth.password.ResetPasswordCodeService;
import org.ebndrnk.leverxfinalproject.service.auth.verify.VerifyEmailService;
import org.ebndrnk.leverxfinalproject.service.mail.send.EmailSender;
import org.ebndrnk.leverxfinalproject.service.mail.template.EmailTemplateGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

/**
 * Implementation of the EmailService.
 * <p>
 * This service provides methods for sending confirmation emails and password reset emails asynchronously.
 * It also generates unique verification and reset codes.
 * </p>
 */
@Slf4j
@Primary
@Service
public class EmailServiceImpl implements EmailService {


    private final EmailSender emailSender;
    private final VerifyEmailService verificationService;
    private final ResetPasswordCodeService resetPasswordCodeService;

    private final EmailTemplateGenerator emailConfirmationTemplateGenerator;
    private final EmailTemplateGenerator passwordResetTemplateGenerator;

    /**
     * Constructor for initializing the EmailServiceImpl with required dependencies.
     *
     * @param emailSender the service responsible for sending emails.
     * @param verifyEmailService the service for handling email verification.
     * @param resetPasswordCodeService the service for handling password reset codes.
     * @param emailConfirmationTemplateGenerator template generator for email verification emails.
     * @param passwordResetTemplateGenerator template generator for password reset emails.
     */
    public EmailServiceImpl(EmailSender emailSender,
                            VerifyEmailService verifyEmailService,
                            ResetPasswordCodeService resetPasswordCodeService,
                            @Qualifier("emailConfirmationTemplateGenerator") EmailTemplateGenerator emailConfirmationTemplateGenerator,
                            @Qualifier("passwordResetTemplateGenerator") EmailTemplateGenerator passwordResetTemplateGenerator) {
        this.emailSender = emailSender;
        this.verificationService = verifyEmailService;
        this.resetPasswordCodeService = resetPasswordCodeService;
        this.emailConfirmationTemplateGenerator = emailConfirmationTemplateGenerator;
        this.passwordResetTemplateGenerator = passwordResetTemplateGenerator;
    }

    /**
     * Asynchronously sends a confirmation email to the provided email address.
     * This method generates a unique verification code, saves it, and sends the email.
     *
     * @param email the email address to send the confirmation email to.
     */
    @Async
    @Override
    public void sendConfirmationEmail(String email) {
        log.info("Sending confirmation email to: {}", email);
        String code = generateVerificationCode();
        verificationService.save(email, code);
        String content = emailConfirmationTemplateGenerator.generateEmailContent(email, code);
        emailSender.sendEmail(email, "LeverX email confirmation", content);
        log.info("Confirmation email sent to: {}", email);
    }

    /**
     * Asynchronously sends a password reset email to the provided email address.
     * This method generates a reset code, saves it, and sends the email.
     *
     * @param email the email address to send the password reset email to.
     */
    @Async
    @Override
    public void sendPasswordResetEmail(String email) {
        log.info("Sending password reset email to: {}", email);
        String code = generateResetCode();
        resetPasswordCodeService.save(email, code);
        String content = passwordResetTemplateGenerator.generateEmailContent(email, code);
        emailSender.sendEmail(email, "Reset your password", content);
        log.info("Password reset email sent to: {}", email);
    }

    /**
     * Generates a unique verification code using UUID.
     *
     * @return a unique string representing the verification code.
     */
    @Override
    public String generateVerificationCode() {
        String verificationCode = UUID.randomUUID().toString();
        log.debug("Generated verification code: {}", verificationCode);
        return verificationCode;
    }

    /**
     * Generates a six-digit random reset code.
     *
     * @return a string representing the reset code.
     */
    @Override
    public String generateResetCode() {
        int code = new Random().nextInt(900000) + 100000;
        log.debug("Generated reset code: {}", code);
        return String.valueOf(code);
    }
}
