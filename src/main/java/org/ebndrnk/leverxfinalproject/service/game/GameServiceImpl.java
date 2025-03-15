package org.ebndrnk.leverxfinalproject.service.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameDto;
import org.ebndrnk.leverxfinalproject.model.dto.game.GamePatchRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.ebndrnk.leverxfinalproject.repository.game.GameRepository;
import org.ebndrnk.leverxfinalproject.repository.specification.GameObjectSpecification;
import org.ebndrnk.leverxfinalproject.service.auth.user.UserService;
import org.ebndrnk.leverxfinalproject.util.component.Patcher;
import org.ebndrnk.leverxfinalproject.util.exception.dto.GameNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
@Slf4j
@Primary
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {


    private final ModelMapper modelMapper;
    private final UserService userService;
    private final GameRepository gameRepository;

    /**
     * FIND A GAME using with filters using specifications
     * @param title
     * @param text
     * @param pageable
     */
    @Override
    public Page<GameResponse> findGameObjects(String title, String text, Pageable pageable) {
        log.info("Fetching game objects with title: '{}' and text: '{}'", title, text);

        Specification<GameObject> spec = Specification.where(GameObjectSpecification.hasTitleLike(title))
                .and(GameObjectSpecification.hasTextLike(text));

        return gameRepository.findAll(spec, pageable)
                .map(gameObject -> modelMapper.map(gameObject, GameResponse.class));
    }

    /**
     * Creates a new game object.
     *
     * @param gameRequest the DTO representing the game object to be created.
     * @return the created {@link GameDto}.
     */
    @Override
    public GameResponse createGameObject(GameRequest gameRequest) {
        log.info("Creating new game object: {}", gameRequest);

        GameObject gameObject = modelMapper.map(gameRequest, GameObject.class);
        gameObject.setSeller(modelMapper.map(userService.getCurrentUser(), User.class).getProfile());
        GameObject savedGameObject = gameRepository.save(gameObject);

        log.info("Created new game object with ID: {}", savedGameObject.getId());
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
    @Override
    public GameResponse updateGameObject(Long gameObjectId, GameRequest gameRequest) {
        log.info("Updating game object with ID: {}", gameObjectId);

        GameObject existingGameObject = gameRepository.findById(gameObjectId)
                .orElseThrow(() -> {
                    log.error("Game object with id {} not found", gameObjectId);
                    return new GameNotFoundException("Game object with id " + gameObjectId + " not found");
                });

        if (!isCurrentUserGameAuthor(gameObjectId)) {
            log.error("User does not have authority to update game object with ID: {}", gameObjectId);
            throw new NoAuthorityForActionException("Only author can update game");
        }

        modelMapper.map(gameRequest, existingGameObject);

        GameObject savedGameObject = gameRepository.save(existingGameObject);

        log.info("Updated game object with ID: {}", savedGameObject.getId());
        return modelMapper.map(savedGameObject, GameResponse.class);
    }

    /**
     * Retrieves a game object by its ID.
     *
     * @param gameObjectId the ID of the game object to retrieve.
     * @return the {@link GameDto} corresponding to the requested game object.
     */
    @Override
    public GameResponse getGameObjectById(Long gameObjectId) {
        log.info("Retrieving game object with ID: {}", gameObjectId);

        GameObject gameObject = gameRepository.findById(gameObjectId)
                .orElseThrow(() -> {
                    log.error("Game object with id {} not found", gameObjectId);
                    return new GameNotFoundException("Game object with id " + gameObjectId + " not found");
                });

        return modelMapper.map(gameObject, GameResponse.class);
    }

    /**
     * Retrieves all game objects.
     *
     * @return a list of {@link GameDto} representing all game objects.
     */
    @Override
    public Page<GameResponse> getAllGameObjects(Pageable pageable) {
        log.info("Fetching all game objects");

        return gameRepository.findAll(pageable)
                .map(gameObject -> modelMapper.map(gameObject, GameResponse.class));
    }

    /**
     * Deletes a game object by its ID.
     *
     * @param gameObjectId the ID of the game object to delete.
     */
    @Override
    public void deleteGameObjectById(Long gameObjectId) {
        log.info("Deleting game object with ID: {}", gameObjectId);

        if (!gameRepository.existsById(gameObjectId)) {
            log.error("Game object with id {} not found", gameObjectId);
            throw new GameNotFoundException("Game object with id " + gameObjectId + " not found");
        }

        if (!isCurrentUserGameAuthor(gameObjectId)) {
            log.error("User does not have authority to delete game object with ID: {}", gameObjectId);
            throw new NoAuthorityForActionException("Only author can delete game");
        }

        gameRepository.deleteById(gameObjectId);

        log.info("Deleted game object with ID: {}", gameObjectId);
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
    @Override
    public GameResponse patchGameObject(Long gameObjectId, GamePatchRequest gameRequest) {
        log.info("Partially updating game object with ID: {}", gameObjectId);
        System.out.println("getPrice: " +gameRequest.getPrice());
        GameObject existingGameObject = gameRepository.findById(gameObjectId)
                .orElseThrow(() -> {
                    log.error("Game object with id {} not found", gameObjectId);
                    return new GameNotFoundException("Game object with id " + gameObjectId + " not found");
                });

        if (!isCurrentUserGameAuthor(gameObjectId)) {
            log.error("User does not have authority to patch game object with ID: {}", gameObjectId);
            throw new NoAuthorityForActionException("Only author can update game");
        }

        GameObject incompleteGameObject = modelMapper.map(gameRequest, GameObject.class);

        try {
            Patcher.patchEntity(existingGameObject, incompleteGameObject);
        } catch (IllegalAccessException e) {
            log.error("Error during patching process", e);
            throw new RuntimeException(e);
        }

        GameObject updatedGameObject = gameRepository.save(existingGameObject);

        log.info("Patched game object with ID: {}", updatedGameObject.getId());
        return modelMapper.map(updatedGameObject, GameResponse.class);
    }

    private boolean isCurrentUserGameAuthor(Long gameId) {
        GameObject gameObject = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game with this id not found"));
        return Objects.equals(gameObject.getSeller().getId(),
                userService.getCurrentUser().getProfile().getId());
    }
}
