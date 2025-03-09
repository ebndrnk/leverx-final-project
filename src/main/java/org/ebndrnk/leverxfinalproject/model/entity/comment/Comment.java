package org.ebndrnk.leverxfinalproject.model.entity.comment;

import jakarta.persistence.*;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "comment")
public class Comment extends BasicEntity {

    @Column(name = "message")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private CommentAuthor author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Profile seller;

    @Column(name = "approved")
    private boolean approved = false;
}
