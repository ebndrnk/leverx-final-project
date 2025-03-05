package org.ebndrnk.leverxfinalproject.model.entity.auth.verify;


import lombok.Data;

import java.io.Serializable;

@Data
public class VerifyEntity implements Serializable {
    private String email;
    private String verifyCode;
}
