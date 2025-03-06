package org.ebndrnk.leverxfinalproject.model.dto.auth;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameObjectDto;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private boolean isConfirmedByAdmin;
    private List<GameObjectDto> gameObjects;
}
