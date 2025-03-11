package org.ebndrnk.leverxfinalproject.model.entity.rating;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rating")
@ToString
@Data
public class Rating extends BasicEntity {
    private byte mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private AnonymousUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Profile seller;
}
