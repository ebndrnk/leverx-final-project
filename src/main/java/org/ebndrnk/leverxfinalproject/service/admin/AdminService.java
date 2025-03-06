package org.ebndrnk.leverxfinalproject.service.admin;

import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;

import java.util.List;

public interface AdminService {
    List<UserResponse> getAll();

    List<UserResponse> getAllNotConfirmedByAdminUsers();

    UserResponse confirmUserByAdmin(Long userId);

    List<UserResponse> confirmAllUsers();

    UserResponse cancelAdminConfirmation(Long userId);

    void confirmEmail(String email);
}
