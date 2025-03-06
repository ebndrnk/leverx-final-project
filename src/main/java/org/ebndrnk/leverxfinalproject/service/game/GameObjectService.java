package org.ebndrnk.leverxfinalproject.service.game;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameObjectDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.ebndrnk.leverxfinalproject.repository.game.GameObjectRepository;
import org.ebndrnk.leverxfinalproject.service.auth.UserServiceImpl;
import org.ebndrnk.leverxfinalproject.util.component.Patcher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
public class GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;

    /**
     * Creates a new game object.
     *
     * @param gameObjectDto the DTO representing the game object to be created.
     * @return the created {@link GameObjectDto}.
     */
    public GameObjectDto createGameObject(GameObjectDto gameObjectDto) {
        GameObject gameObject = modelMapper.map(gameObjectDto, GameObject.class);
        gameObject.setUser(modelMapper.map(userService.getCurrentUser(), User.class));
        GameObject savedGameObject = gameObjectRepository.save(gameObject);
        return modelMapper.map(savedGameObject, GameObjectDto.class);
    }

    /**
     * Updates an existing game object.
     * <p>
     * The game object is updated with the provided DTO data. The ID from the URL is set to ensure the correct entity is modified.
     * </p>
     *
     * @param gameObjectId  the ID of the game object to update.
     * @param gameObjectDto the DTO containing updated game object data.
     * @return the updated {@link GameObjectDto}.
     */
    public GameObjectDto updateGameObject(Long gameObjectId, GameObjectDto gameObjectDto) {
        GameObject existingGameObject = gameObjectRepository.findById(gameObjectId)
                .orElseThrow(() -> new NoSuchElementException("Game object with id " + gameObjectId + " not found"));

        modelMapper.map(gameObjectDto, existingGameObject);

        GameObject updatedGameObject = gameObjectRepository.save(existingGameObject);

        return modelMapper.map(updatedGameObject, GameObjectDto.class);
    }


    /**
     * Retrieves a game object by its ID.
     *
     * @param gameObjectId the ID of the game object to retrieve.
     * @return the {@link GameObjectDto} corresponding to the requested game object.
     */
    public GameObjectDto getGameObjectById(Long gameObjectId) {
        GameObject gameObject = gameObjectRepository.findById(gameObjectId)
                .orElseThrow(() -> new NoSuchElementException("Game object with id " + gameObjectId + " not found"));
        return modelMapper.map(gameObject, GameObjectDto.class);
    }

    /**
     * Retrieves all game objects.
     *
     * @return a list of {@link GameObjectDto} representing all game objects.
     */
    public List<GameObjectDto> getAllGameObjects() {
        return gameObjectRepository.findAll()
                .stream()
                .map(gameObject -> modelMapper.map(gameObject, GameObjectDto.class))
                .toList();
    }

    /**
     * Deletes a game object by its ID.
     *
     * @param gameObjectId the ID of the game object to delete.
     */
    public void deleteGameObjectById(Long gameObjectId) {
        if (!gameObjectRepository.existsById(gameObjectId)) {
            throw new NoSuchElementException("Game object with id " + gameObjectId + " not found");
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
     * @param gameObjectDto the DTO containing fields to be patched.
     * @return the updated {@link GameObjectDto}.
     * @throws IllegalAccessException      if the patching process fails due to inaccessible fields.
     */
    public GameObjectDto patchGameObject(Long gameObjectId, GameObjectDto gameObjectDto) throws IllegalAccessException {
        GameObject existingGameObject = gameObjectRepository.findById(gameObjectId)
                .orElseThrow(() -> new NoSuchElementException("Game object with id " + gameObjectId + " not found"));

        GameObject incompleteGameObject = modelMapper.map(gameObjectDto, GameObject.class);

        Patcher.patchEntity(existingGameObject, incompleteGameObject);

        GameObject updatedGameObject = gameObjectRepository.save(existingGameObject);
        return modelMapper.map(updatedGameObject, GameObjectDto.class);
    }
}
