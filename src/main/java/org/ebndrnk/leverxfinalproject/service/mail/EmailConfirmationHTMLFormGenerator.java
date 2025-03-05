package org.ebndrnk.leverxfinalproject.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for generating HTML content for email confirmation.
 * <p>
 * This class loads an HTML template from a file and replaces the placeholder
 * "{verificationUrl}" with the provided verification URL.
 * </p>
 */
@Component
public class EmailConfirmationHTMLFormGenerator {

    private static final String TEMPLATE_PATH = "/templates/email-confirmation.html";
    private static final String AUTH_CODE_PREFIX = "/verify?verifyCode=";
    private static final String EMAIL_PREFIX = "&email=";

    @Value("${site.domain.url}")
    private String domainUrl;

    /**
     * Generates the HTML content for an email confirmation by loading a template file
     * and replacing the placeholder "{verificationUrl}" with the specified URL.
     *
     * @param email      the user's email address.
     * @param verifyCode the verification code.
     * @return the generated HTML content as a String.
     * @throws RuntimeException if the template file cannot be found or read.
     */
    public String generateHTMLForm(String email, String verifyCode) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(TEMPLATE_PATH)) {
            if (inputStream == null) {
                throw new RuntimeException("Template file not found: " + TEMPLATE_PATH);
            }
            String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return template.replace("{verificationUrl}", generateVerificationUrl(email, verifyCode));
        } catch (IOException e) {
            throw new RuntimeException("Error reading email confirmation template", e);
        }
    }

    /**
     * Constructs the verification URL based on the verification code and email.
     *
     * @param email      the user's email address.
     * @param verifyCode the verification code.
     * @return the complete verification URL.
     */
    private String generateVerificationUrl(String email, String verifyCode) {
        return domainUrl + AUTH_CODE_PREFIX + verifyCode + EMAIL_PREFIX + email;
    }
}
