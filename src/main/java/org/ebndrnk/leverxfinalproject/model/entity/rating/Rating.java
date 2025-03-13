package org.ebndrnk.leverxfinalproject.model.entity.rating;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.hibernate.annotations.Comment;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rating")
@ToString
@Data
public class Rating extends BasicEntity {
    @Column(name = "mark")
    @Comment("number from 0 to 10")
    private byte mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @Comment("anonyomus user made it")
    private AnonymousUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @Comment("seller wich was rated by anonymous user")
    private Profile seller;
}
