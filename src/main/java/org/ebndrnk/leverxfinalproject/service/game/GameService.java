package org.ebndrnk.leverxfinalproject.service.game;

import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {
    Page<GameResponse> findGameObjects(String title, String text, Pageable pageable);

    GameResponse createGameObject(GameRequest gameRequest);

    GameResponse updateGameObject(Long gameObjectId, GameRequest gameRequest);

    GameResponse getGameObjectById(Long gameObjectId);

    Page<GameResponse> getAllGameObjects(Pageable pageable);

    void deleteGameObjectById(Long gameObjectId);

    GameResponse patchGameObject(Long gameObjectId, GameRequest gameRequest);
}
