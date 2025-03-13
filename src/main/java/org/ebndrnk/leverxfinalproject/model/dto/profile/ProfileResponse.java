package org.ebndrnk.leverxfinalproject.model.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;

import java.util.List;

@Data
public class ProfileResponse {

    @Schema(description = "Unique identifier of the profile")
    private Long id;

    @Schema(description = "Username of the profile owner")
    private String username;

    @Schema(description = "First name of the profile owner")
    private String firstname;

    @Schema(description = "Last name of the profile owner")
    private String lastname;

    @Schema(description = "Email of the profile owner")
    private String email;

    @Schema(description = "List of comments made by users on the profile")
    private List<CommentResponse> comment;

    @Schema(description = "Indicates if the profile is confirmed by an admin")
    private boolean isConfirmedByAdmin;

    @Schema(description = "List of games associated with the profile")
    private List<GameResponse> gameObjects;

    @Schema(description = "Rating of the profile, ranging from 0 to 5")
    private byte rating;
}
