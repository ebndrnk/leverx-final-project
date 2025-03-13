package org.ebndrnk.leverxfinalproject.model.entity.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "profile")
@ToString
@Data
public class Profile extends BasicEntity {

    @org.hibernate.annotations.Comment("Unique username. Must be between 4 and 50 characters.")
    @Column(name = "USERNAME", length = 50, unique = true)
    @Size(min = 4, max = 50)
    private String username;

    @org.hibernate.annotations.Comment("User first name. Must be between 4 and 50 characters.")
    @Column(name = "FIRSTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String firstname;

    @org.hibernate.annotations.Comment("User last name. Must be between 4 and 50 characters.")
    @Column(name = "LASTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String lastname;

    @org.hibernate.annotations.Comment("User email address. Must be valid and between 4 and 50 characters.")
    @Column(name = "EMAIL", length = 50)
    @Size(min = 4, max = 50)
    @Email
    private String email;

    @org.hibernate.annotations.Comment("List of comments made by the user.")
    @Column(name = "comment", columnDefinition = "TEXT")
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @org.hibernate.annotations.Comment("Flag indicating whether the profile is confirmed by an admin.")
    @Column(name = "is_confirmed_by_admin", nullable = false)
    private boolean isConfirmedByAdmin = false;

    @org.hibernate.annotations.Comment("List of game objects associated with the user.")
    @OneToMany(mappedBy = "seller")
    private List<GameObject> gameObjects;

    @org.hibernate.annotations.Comment("Users rating. Default is 0.")
    @Column(name = "rating")
    private byte rating = 0;
}