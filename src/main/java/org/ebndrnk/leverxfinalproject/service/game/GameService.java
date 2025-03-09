package org.ebndrnk.leverxfinalproject.service.game;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameDto;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.ebndrnk.leverxfinalproject.repository.game.GameRepository;
import org.ebndrnk.leverxfinalproject.service.auth.user.UserServiceImpl;
import org.ebndrnk.leverxfinalproject.util.component.Patcher;
import org.ebndrnk.leverxfinalproject.util.exception.dto.GameNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Service class for managing game objects.
 * <p>
 * This service provides methods to create, update, retrieve, and delete game objects.
 * It uses a ModelMapper for converting between DTOs and entities, and delegates
 * user-related logic to the UserService.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameObjectRepository;
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;
    private final GameRepository gameRepository;

    /**
     * Creates a new game object.
     *
     * @param gameRequest the DTO representing the game object to be created.
     * @return the created {@link GameDto}.
     */
    public GameResponse createGameObject(GameRequest gameRequest) {
        GameObject gameObject = modelMapper.map(gameRequest, GameObject.class);
        gameObject.setSeller(modelMapper.map(userService.getCurrentUser(), User.class).getProfile());
        GameObject savedGameObject = gameObjectRepository.save(gameObject);
        return modelMapper.map(savedGameObject, GameResponse.class);
    }

    /**
     * Updates an existing game object.
     * <p>
     * The game object is updated with the provided DTO data. The ID from the URL is used to ensure the correct entity is modified.
     * </p>
     *
     * @param gameObjectId  the ID of the game object to update.
     * @param gameRequest   the DTO containing updated game object data.
     * @return the updated {@link GameDto}.
     */
    public GameResponse updateGameObject(Long gameObjectId, GameRequest gameRequest) {
        GameObject existingGameObject = gameObjectRepository.findById(gameObjectId)
                .orElseThrow(() -> new GameNotFoundException("Game object with id " + gameObjectId + " not found"));

        if(!isCurrentUserGameAuthor(gameObjectId)){
            throw new NoAuthorityForActionException("Only author can update game");
        }

        modelMapper.map(gameRequest, existingGameObject);

        GameObject savedGameObject = gameObjectRepository.save(existingGameObject);

        return modelMapper.map(savedGameObject, GameResponse.class);
    }



    /**
     * Retrieves a game object by its ID.
     *
     * @param gameObjectId the ID of the game object to retrieve.
     * @return the {@link GameDto} corresponding to the requested game object.
     */
    public GameResponse getGameObjectById(Long gameObjectId) {
        GameObject gameObject = gameObjectRepository.findById(gameObjectId)
                .orElseThrow(() -> new GameNotFoundException("Game object with id " + gameObjectId + " not found"));
        return modelMapper.map(gameObject, GameResponse.class);
    }

    /**
     * Retrieves all game objects.
     *
     * @return a list of {@link GameDto} representing all game objects.
     */
    public List<GameResponse> getAllGameObjects() {
        return gameObjectRepository.findAll()
                .stream()
                .map(gameObject -> modelMapper.map(gameObject, GameResponse.class))
                .toList();
    }

    /**
     * Deletes a game object by its ID.
     *
     * @param gameObjectId the ID of the game object to delete.
     */
    public void deleteGameObjectById(Long gameObjectId) {
        if (!gameObjectRepository.existsById(gameObjectId)) {
            throw new GameNotFoundException("Game object with id " + gameObjectId + " not found");
        }

        if(!isCurrentUserGameAuthor(gameObjectId)){
            throw new NoAuthorityForActionException("Only author can delete game");
        }
        gameObjectRepository.deleteById(gameObjectId);
    }

    /**
     * Partially updates a game object.
     * <p>
     * This method patches the existing game object with non-null values from the provided DTO.
     * The incomplete DTO is mapped to a temporary entity, which is then used to patch the existing entity using the generic {@link Patcher}.
     * Finally, the updated entity is saved and converted back to a DTO.
     * </p>
     *
     * @param gameObjectId  the ID of the game object to update.
     * @param gameRequest the DTO containing fields to be patched.
     * @return the updated {@link GameDto}.
     * @throws IllegalAccessException      if the patching process fails due to inaccessible fields.
     */
    public GameResponse patchGameObject(Long gameObjectId, GameRequest gameRequest) {
        GameObject existingGameObject = gameObjectRepository.findById(gameObjectId)
                .orElseThrow(() -> new GameNotFoundException("Game object with id " + gameObjectId + " not found"));

        if(!isCurrentUserGameAuthor(gameObjectId)){
            throw new NoAuthorityForActionException("Only author can update game");
        }

        GameObject incompleteGameObject = modelMapper.map(gameRequest, GameObject.class);

        try {
            Patcher.patchEntity(existingGameObject, incompleteGameObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        GameObject updatedGameObject = gameObjectRepository.save(existingGameObject);
        return modelMapper.map(updatedGameObject, GameResponse.class);
    }

    private boolean isCurrentUserGameAuthor(Long gameId){
        GameObject gameObject = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game with this id not found"));
        return Objects.equals(gameObject.getSeller().getId(),
                userService.getCurrentUser().getProfile().getId());

    }
}
