package org.ebndrnk.leverxfinalproject.model.dto.password;

public record ResetCodeVerificationRequest(String userEmail, String code) {
}
