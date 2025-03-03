package org.ebndrnk.leverxfinalproject.model.entity.game;

import jakarta.persistence.*;
import lombok.*;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "game_object")
public class GameObject extends BasicEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
