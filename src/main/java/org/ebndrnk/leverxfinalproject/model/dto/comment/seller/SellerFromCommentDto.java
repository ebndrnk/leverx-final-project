package org.ebndrnk.leverxfinalproject.model.dto.comment.seller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;

@Schema(description = "DTO representing a seller associated with a comment")
@Data
public class SellerFromCommentDto {

    @Schema(description = "ID of the seller", example = "12345")
    private Long id;

    @Schema(description = "Username of the seller", example = "seller123")
    private String username;

    @Schema(description = "First name of the seller", example = "John")
    private String firstname;

    @Schema(description = "Last name of the seller", example = "Doe")
    private String lastname;

    @Schema(description = "Email address of the seller", example = "seller123@example.com")
    private String email;

    @Schema(description = "Profile information of the seller")
    private ProfileDto profile;
}
