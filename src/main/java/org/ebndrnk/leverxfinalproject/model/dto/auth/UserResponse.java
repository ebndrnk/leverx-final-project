package org.ebndrnk.leverxfinalproject.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameDto;

import java.util.List;

@Schema(description = "Response DTO representing a user in the system")
@Data
public class UserResponse {

    @Schema(description = "ID of the user", example = "12345")
    private Long id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "First name of the user", example = "John")
    private String firstname;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastname;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Flag indicating if the user is confirmed by admin")
    private boolean isConfirmedByAdmin;

    @Schema(description = "List of game objects associated with the user")
    private List<GameDto> gameObjects;
}
