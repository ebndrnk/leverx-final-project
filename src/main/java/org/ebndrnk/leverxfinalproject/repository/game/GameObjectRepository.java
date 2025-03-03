package org.ebndrnk.leverxfinalproject.repository.game;

import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, Long> {
}
