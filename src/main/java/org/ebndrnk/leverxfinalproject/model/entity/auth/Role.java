package org.ebndrnk.leverxfinalproject.model.entity.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_SELLER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
