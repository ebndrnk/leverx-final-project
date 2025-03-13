package org.ebndrnk.leverxfinalproject.model.dto.comment.seller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SellerFromCommentRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Schema(description = "Username of the seller", example = "seller123")
    private String username;

    @Size(min = 4, max = 50, message = "First name must be between 4 and 50 characters")
    @Schema(description = "First name of the seller", example = "John")
    private String firstname;

    @Size(min = 4, max = 50, message = "Last name must be between 4 and 50 characters")
    @Schema(description = "Last name of the seller", example = "Doe")
    private String lastname;

    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the seller", example = "seller123@example.com")
    private String email;

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 4, message = "Message must be at least 3 characters long")
    @Schema(description = "Message from the seller", example = "I would like to leave a comment.")
    private String message;
}
