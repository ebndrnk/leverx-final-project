package org.ebndrnk.leverxfinalproject.model.dto.comment.seller;

import lombok.Data;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;

@Data
public class SellerFromCommentDto {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private ProfileDto profile;
}
