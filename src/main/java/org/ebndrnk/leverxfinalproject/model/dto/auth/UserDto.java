package org.ebndrnk.leverxfinalproject.model.dto.auth;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;

@Data
public class UserDto {
    private ProfileDto profile;
    private String username;
    private Long id;
    private String password;
    private String email;
    private boolean isEmailConfirmed;
    private Role role;
}
