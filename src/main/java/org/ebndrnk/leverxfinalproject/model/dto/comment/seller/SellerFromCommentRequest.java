package org.ebndrnk.leverxfinalproject.model.dto.comment.seller;

import lombok.Data;

@Data
public class SellerFromCommentRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String message;
}
