package org.ebndrnk.leverxfinalproject.model.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 3, message = "Message must be at least 3 characters long")
    private String message;
}
