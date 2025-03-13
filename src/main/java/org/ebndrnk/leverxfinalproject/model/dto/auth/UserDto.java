package org.ebndrnk.leverxfinalproject.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;

@Schema(description = "DTO representing a user in the system")
@Data
public class UserDto {

    @Schema(description = "Profile details of the user")
    private ProfileDto profile;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "ID of the user", example = "12345")
    private Long id;

    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Flag indicating if the user's email is confirmed")
    private boolean isEmailConfirmed;

    @Schema(description = "Role assigned to the user")
    private Role role;
}
