package org.ebndrnk.leverxfinalproject.service.mail.template;

public interface EmailTemplateGenerator {
    String generateEmailContent(String email, String code);
}
