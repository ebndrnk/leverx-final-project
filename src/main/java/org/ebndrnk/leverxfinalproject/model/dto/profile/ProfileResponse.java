package org.ebndrnk.leverxfinalproject.model.dto.profile;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;

import java.util.List;

@Data
public class ProfileResponse {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private List<CommentResponse> comment;
    private boolean isConfirmedByAdmin;
    private List<GameResponse> gameObjects;
}
