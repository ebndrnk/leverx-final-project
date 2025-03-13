package org.ebndrnk.leverxfinalproject.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegistrationResponse {
    @Schema(description = "result of the sending confirmation email")
    private String result = "mail sent successfully!";
}
