package org.ebndrnk.leverxfinalproject.service.admin;

import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;

import java.util.List;

public interface AdminService {
    List<ProfileResponse> getAll();

    List<ProfileResponse> getAllNotConfirmedByAdminUsers();

    ProfileResponse confirmUserByAdmin(Long userId);

    List<ProfileResponse> confirmAllUsers();

    ProfileResponse cancelAdminConfirmation(Long userId);

    void confirmEmail(String email);
}
