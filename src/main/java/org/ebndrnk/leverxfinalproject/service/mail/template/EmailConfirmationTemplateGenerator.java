package org.ebndrnk.leverxfinalproject.service.mail.template;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailConfirmationTemplateGenerator implements EmailTemplateGenerator {

    private final SpringTemplateEngine templateEngine;

    @Value("${site.domain.url}")
    private String domainUrl;


    @Override
    public String generateEmailContent(String email, String code) {
        Context context = new Context();
        String verificationUrl = domainUrl + "/verify?verifyCode=" + code + "&email=" + email;
        context.setVariable("verificationUrl", verificationUrl);
        return templateEngine.process("email-confirmation", context);
    }
}
