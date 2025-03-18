package org.ebndrnk.leverxfinalproject.service.account.user;

import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserSecurityPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDto save(UserDto user);

    UserDto getById(Long userId);

    UserSecurityPrincipal getByUsername(String username);

    UserDetailsService userDetailsService();

    UserDto getCurrentUser();

    UserDto getByUserEmail(String email);

    boolean isEmailConfirmed(String username);

    User findByEmail(String email);
}
