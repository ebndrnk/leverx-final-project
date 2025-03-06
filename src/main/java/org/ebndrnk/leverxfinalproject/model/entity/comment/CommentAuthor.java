package org.ebndrnk.leverxfinalproject.model.entity.comment;

import jakarta.persistence.*;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "commentList")
@Table(name = "comment_author")
public class CommentAuthor extends BasicEntity {

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @Column(name = "identifier")
    private String identifier;
}
