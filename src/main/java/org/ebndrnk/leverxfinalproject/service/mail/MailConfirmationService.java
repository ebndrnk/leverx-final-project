package org.ebndrnk.leverxfinalproject.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.service.auth.verify.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for sending email confirmation messages.
 * <p>
 * This service generates a verification code, saves it via the verification service,
 * creates an HTML email message, and sends it using JavaMailSender.
 * </p>
 */
@Service
@Primary
@RequiredArgsConstructor
public class MailConfirmationService implements MailService {

    private final JavaMailSender javaMailSender;
    private final VerificationService verificationService;
    private final EmailConfirmationHTMLFormGenerator formGenerator;

    @Value("${spring.mail.username}")
    private String mailFrom;



    /**
     * Sends an HTML email for confirmation.
     *
     * @param userEmail the recipient's email address.
     */
    @Override
    public void send(String userEmail) {
        String verifyCode = generateVerificationCode();
        verificationService.save(userEmail, verifyCode);

        try {
            javaMailSender.send(createMessage(userEmail, verifyCode));
        } catch (Exception ex) {
            throw new RuntimeException("Error sending confirmation email", ex);
        }
    }

    /**
     * Creates a MimeMessage for sending the confirmation email.
     * <p>
     * This method is provided for interface compatibility, though the send(String) method
     * is primarily used to dispatch the HTML email.
     * </p>
     *
     * @param userEmail  the recipient's email address.
     * @param verifyCode the verification code.
     * @return the created MimeMessage.
     */
    @Override
    public MimeMessage createMessage(String userEmail, String verifyCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(userEmail);
            helper.setSubject("LeverX email confirmation");

            String htmlContent = formGenerator.generateHTMLForm(userEmail, verifyCode);
            helper.setText(htmlContent, true);

            return mimeMessage;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Generates a unique verification code.
     *
     * @return a unique string representing the verification code.
     */
    private String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }

}
