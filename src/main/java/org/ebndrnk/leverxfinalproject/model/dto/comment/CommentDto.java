package org.ebndrnk.leverxfinalproject.model.dto.comment;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;

@Data
public class CommentDto {
    private Long id;
    private String message;
    private boolean approved;
    private UserDto seller;
}
