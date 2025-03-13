package org.ebndrnk.leverxfinalproject.model.entity.anonymous;

import jakarta.persistence.*;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "commentList")
@Table(name = "anonymous_user")
public class AnonymousUser extends BasicEntity {

    @org.hibernate.annotations.Comment("The list of comments made by the anonymous user. This field is excluded from the toString method to avoid circular references.")
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @org.hibernate.annotations.Comment("The unique identifier for the anonymous user(cookies). This field is required and cannot be null.")
    @Column(name = "anonymous_id", unique = true, nullable = false)
    private String anonymousId;
}