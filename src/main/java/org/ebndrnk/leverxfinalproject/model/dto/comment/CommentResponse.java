package org.ebndrnk.leverxfinalproject.model.dto.comment;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;

@Data
public class CommentResponse {
    private Long id;
    private String message;
    private boolean approved;
    private UserResponse seller;
}
