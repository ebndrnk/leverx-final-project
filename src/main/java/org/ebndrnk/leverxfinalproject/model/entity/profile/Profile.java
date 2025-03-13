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
    @Column(name = "USERNAME", length = 50, unique = true)
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "FIRSTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50)
    @Size(min = 4, max = 50)
    @Email
    private String email;

    @Column(name = "comment", columnDefinition = "TEXT")
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @Column(name = "is_confirmed_by_admin", nullable = false)
    private boolean isConfirmedByAdmin = false;

    @OneToMany(mappedBy = "seller")
    private List<GameObject> gameObjects;

    @Column(name = "rating")
    private byte rating = 0;
}
