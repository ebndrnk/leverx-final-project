package org.ebndrnk.leverxfinalproject.model.entity.comment.seller;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "seller_from_comment")
public class SellerFromComment extends BasicEntity {

    @Comment("The profile associated with the seller. This field is linked to the Profile entity.")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @Comment("The username of the seller. Must be between 4 and 50 characters.")
    @Size(min = 4, max = 50, message = "Username must contain from 4 to 50 characters")
    @Column(name = "username")
    private String username;

    @Comment("The first name of the seller. Must be between 4 and 50 characters.")
    @Size(min = 4, max = 50, message = "Username must contain from 4 to 50 characters")
    @Column(name = "firstname")
    private String firstname;

    @Comment("The last name of the seller. Must be between 4 and 50 characters.")
    @Size(min = 4, max = 50, message = "The user's last name must contain from 4 to 50 characters")
    @Column(name = "lastname")
    private String lastname;

    @Comment("The email address of the seller. Must be between 4 and 50 characters.")
    @Size(min = 4, max = 50, message = "User email must contain from 4 to 50 characters")
    @Column(name = "email")
    private String email;
}