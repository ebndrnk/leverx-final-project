package org.ebndrnk.leverxfinalproject.model.entity.game;

import jakarta.persistence.*;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "game_object")
public class GameObject extends BasicEntity {

    @Comment("The title of the game. This field is required and cannot be null.")
    @Column(name = "title", nullable = false)
    private String title;

    @Comment("A detailed description of the game. This field is required and cannot be null.")
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @Comment("The seller (profile) who is offering the game. This field is required and cannot be null.")
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Profile seller;

    @Comment("The price of the game. This field is required and cannot be null.")
    @Column(name = "price", nullable = false)
    private Float price;

}