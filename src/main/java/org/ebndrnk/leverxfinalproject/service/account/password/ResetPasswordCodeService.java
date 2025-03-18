package org.ebndrnk.leverxfinalproject.service.account.password;

public interface ResetPasswordCodeService {
    void save(String userEmail, String verifyCode);
    boolean verify(String userEmail, String verifyCode);
    void delete(String userEmail);
}
