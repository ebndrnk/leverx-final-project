package org.ebndrnk.leverxfinalproject.service.auth.user;

import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDto save(UserDto user);

    UserDto getById(Long userId);

    UserPrincipalImpl getByUsername(String username);

    UserDetailsService userDetailsService();

    UserDto getCurrentUser();

    UserDto getByUserEmail(String email);

    boolean isEmailConfirmed(String username);
}
