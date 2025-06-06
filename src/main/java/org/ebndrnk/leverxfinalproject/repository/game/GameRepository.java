package org.ebndrnk.leverxfinalproject.repository.game;

import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepository extends JpaRepository<GameObject, Long>, JpaSpecificationExecutor<GameObject> {
}
