package org.ebndrnk.leverxfinalproject.model.dto.auth;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameObjectDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String password;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private boolean isConfirmedByAdmin;
    private boolean isEmailConfirmed;
    private Role role;
    private List<GameObjectDto> gameObjects;
}
