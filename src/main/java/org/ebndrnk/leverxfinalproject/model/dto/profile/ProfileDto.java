package org.ebndrnk.leverxfinalproject.model.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameDto;

import java.util.List;

@Data
public class ProfileDto {

    @Schema(description = "ID of the profile", example = "123")
    private Long id;

    @Schema(description = "Username of the profile", example = "john_doe")
    private String username;

    @Schema(description = "First name of the user", example = "John")
    private String firstname;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastname;

    @Schema(description = "Mark or rating of the profile", example = "5")
    private byte mark;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "List of comments related to the profile")
    private List<CommentDto> comment;

    @Schema(description = "Indicates if the profile is confirmed by an admin", example = "false")
    private boolean isConfirmedByAdmin = false;

    @Schema(description = "List of game objects associated with the profile")
    private List<GameDto> gameObjects;
}
