package org.ebndrnk.leverxfinalproject.model.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;

@Data
public class CommentDto {

    @Schema(description = "Unique identifier for the comment", example = "1")
    private Long id;

    @Schema(description = "Message content of the comment", example = "Great product, I really liked it!")
    private String message;

    @Schema(description = "Indicates if the comment has been approved by an admin", example = "true")
    private boolean approved;

    @Schema(description = "Profile of the seller associated with the comment")
    private ProfileDto seller;
}
