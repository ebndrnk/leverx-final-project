package org.ebndrnk.leverxfinalproject.service.auth;

import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;

import java.util.List;

public interface UserService {
    UserDto save(UserDto user);

    UserDto getById(Long userId);

    UserPrincipalImpl getByUsername(String username);

    UserDto getCurrentUser();

    UserDto getByUserEmail(String email);
}
