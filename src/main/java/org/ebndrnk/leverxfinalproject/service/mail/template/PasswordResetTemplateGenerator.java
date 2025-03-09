package org.ebndrnk.leverxfinalproject.service.mail.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class PasswordResetTemplateGenerator implements EmailTemplateGenerator {

    private final SpringTemplateEngine templateEngine;


    @Override
    public String generateEmailContent(String email, String code) {
        Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process("password-reset", context);
    }
}
