package org.ebndrnk.leverxfinalproject.model.entity.comment;

import jakarta.persistence.*;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment extends BasicEntity {

    @org.hibernate.annotations.Comment("The message content of the comment. This field is required and cannot be null.")
    @Column(name = "message", nullable = false)
    private String message;

    @org.hibernate.annotations.Comment("The anonymous user who authored the comment. This field is optional.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private AnonymousUser author;

    @org.hibernate.annotations.Comment("The seller (profile) to whom the comment is addressed. This field is required and cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Profile seller;

    @org.hibernate.annotations.Comment("A flag indicating whether the comment has been approved by an admin. Default is false.")
    @Column(name = "approved")
    private boolean approved = false;
}