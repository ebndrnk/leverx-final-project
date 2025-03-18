package org.ebndrnk.leverxfinalproject.service.account.verify;

public interface VerifyEmailService {
    void save(String userEmail, String verifyCode);
    boolean verify(String userEmail, String verifyCode);
    void confirmEmail(String userEmail);
    void delete(String userEmail);
}
