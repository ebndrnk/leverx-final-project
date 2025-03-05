package org.ebndrnk.leverxfinalproject.service.auth;

import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;

public interface UserService {
    User save(User user);
    UserPrincipalImpl getByUsername(String username);
    User getCurrentUser();
    User getByUserEmail(String email);
}
