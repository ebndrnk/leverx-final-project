package org.ebndrnk.leverxfinalproject.model.dto.profile;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameDto;

import java.util.List;

@Data
public class ProfileDto {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private List<CommentDto> comment;
    private boolean isConfirmedByAdmin = false;
    private List<GameDto> gameObjects;
}
