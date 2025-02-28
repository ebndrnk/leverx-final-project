package org.ebndrnk.leverxfinalproject.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Size(min = 4, max = 50, message = "Username must contain from 4 to 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Size(min = 4, max = 255, message = "Password length must be no more than 255 characters")
    private String password;

    @Size(min = 4, max = 50, message = "Username must contain from 4 to 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String firstname;

    @Size(min = 4, max = 50, message = "The user's last name must contain from 4 to 50 characters")
    @NotBlank(message = "User's last name cannot be empty")
    private String lastname;

    @Size(min = 4, max = 50, message = "User email must contain from 4 to 50 characters")
    @NotBlank(message = "User email cannot be empty")
    private String email;
}
