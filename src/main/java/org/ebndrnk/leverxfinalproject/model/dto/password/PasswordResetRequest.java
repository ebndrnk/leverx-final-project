package org.ebndrnk.leverxfinalproject.model.dto.password;

public record PasswordResetRequest(String userEmail, String code, String newPassword) {
}
