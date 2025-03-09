package org.ebndrnk.leverxfinalproject.model.entity.comment.seller;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "seller_from_comment")
public class SellerFromComment extends BasicEntity{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @Size(min = 4, max = 50, message = "Username must contain from 4 to 50 characters")
    @Column(name = "username")
    private String username;

    @Size(min = 4, max = 50, message = "Username must contain from 4 to 50 characters")
    @Column(name = "firstname")
    private String firstname;

    @Size(min = 4, max = 50, message = "The user's last name must contain from 4 to 50 characters")
    @Column(name = "lastname")
    private String lastname;

    @Size(min = 4, max = 50, message = "User email must contain from 4 to 50 characters")
    @Column(name = "email")
    private String email;
}
