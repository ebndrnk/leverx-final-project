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

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @Column(name = "anonymous_id", unique = true, nullable = false)
    private String anonymousId;
}
