package org.ebndrnk.leverxfinalproject.model.dto.comment;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;

@Data
public class CommentResponse {
    private Long id;
    private String message;
    private boolean approved;
    private Long sellerId;
}
