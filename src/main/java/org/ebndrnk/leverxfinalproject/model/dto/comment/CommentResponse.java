package org.ebndrnk.leverxfinalproject.model.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentResponse {

    @Schema(description = "Unique identifier of the comment", example = "1")
    private Long id;

    @Schema(description = "Message content of the comment", example = "Great product!")
    private String message;

    @Schema(description = "Status of comment approval", example = "true")
    private boolean approved;

    @Schema(description = "Unique identifier of the seller", example = "12345")
    private Long sellerId;
}
