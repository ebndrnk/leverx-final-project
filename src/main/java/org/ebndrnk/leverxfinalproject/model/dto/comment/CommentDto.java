package org.ebndrnk.leverxfinalproject.model.dto.comment;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;

@Data
public class CommentDto {
    private Long id;
    private String message;
    private boolean approved;
    private ProfileDto seller;
}
