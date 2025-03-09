package org.ebndrnk.leverxfinalproject.model.entity.auth.password;

import lombok.Data;

@Data
public class ResetPasswordCodeEntity {
    private String email;
    private String code;
}
