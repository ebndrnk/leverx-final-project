package org.ebndrnk.leverxfinalproject.service.auth.verify;

public interface VerificationService {
    void save(String userEmail, String verifyCode);
    boolean verify(String userEmail, String verifyCode);
    void confirmEmail(String userEmail);
}
