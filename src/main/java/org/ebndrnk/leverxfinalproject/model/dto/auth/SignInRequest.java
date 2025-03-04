package org.ebndrnk.leverxfinalproject.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {

    @Size(min = 4, max = 50, message = "Email must contain from 4 to 50 characters")
    @NotBlank(message = "Email cannot be empty")
    @Schema(description = "user email")
    private String email;

    @Size(max = 255, message = "Password length must be no more than 255 characters")
    @Schema(description = "user password")
    private String password;
}
