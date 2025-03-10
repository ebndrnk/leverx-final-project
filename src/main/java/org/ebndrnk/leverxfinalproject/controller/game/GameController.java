package org.ebndrnk.leverxfinalproject.controller.game;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameDto;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.service.game.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller for managing CRUD operations on game objects.
 * <p>
 * This REST controller provides endpoints to create, retrieve, update, and delete game objects.
 * It demonstrates best practices such as:
 * <ul>
 *   <li>Proper use of HTTP status codes and headers (e.g., 201 Created with Location header for new resources).</li>
 *   <li>Validation of incoming request payloads using Jakarta Bean Validation.</li>
 *   <li>Clear separation of concerns with service layer delegation.</li>
 *   <li>Comprehensive API documentation via Swagger/OpenAPI annotations.</li>
 * </ul>
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameObjectService;

    /**
     * Creates a new game object.
     * <p>
     * Accepts a JSON representation of the game object and creates a new resource.
     * Returns a 201 Created status along with the URI of the newly created resource.
     * </p>
     *
     * @param gameRequest the game object data transfer object to be created.
     * @return a ResponseEntity containing the created GameObjectDto and a Location header.
     */
    @Operation(summary = "Create Game Object", description = "Creates a new game object and returns the created resource with a location header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game object created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<GameResponse> createGameObject(@Valid @RequestBody GameRequest gameRequest) {
        GameResponse createdDto = gameObjectService.createGameObject(gameRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdDto);
    }

    /**
     * Updates an existing game object.
     * <p>
     * Updates the game object identified by the provided ID with new data.
     * Returns the updated game object along with a Location header indicating the resource's URI.
     * </p>
     *
     * @param id            the ID of the game object to update.
     * @param gameRequest the game object data transfer object containing updated information.
     * @return a ResponseEntity containing the updated GameObjectDto.
     */
    @Operation(summary = "Update Game Object", description = "Updates an existing game object identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game object updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Game object not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGameObject(@PathVariable(name = "id") Long id,
                                                         @Valid @RequestBody GameRequest gameRequest) {
        GameResponse updatedDto = gameObjectService.updateGameObject(id, gameRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(updatedDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(updatedDto);
    }

    /**
     * Retrieves a game object by its ID.
     * <p>
     * Fetches the game object corresponding to the specified ID.
     * </p>
     *
     * @param id the ID of the game object to retrieve.
     * @return a ResponseEntity containing the requested GameObjectDto.
     */
    @Operation(summary = "Get Game Object", description = "Retrieves a game object by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game object retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game object not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGameObject(@PathVariable(name = "id") Long id) {
        GameResponse dto = gameObjectService.getGameObjectById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all game objects.
     * <p>
     * Returns a list of all game objects available in the system.
     * </p>
     *
     * @return a ResponseEntity containing a list of GameObjectDto objects.
     */
    @Operation(summary = "List Game Objects", description = "Retrieves a list of all game objects.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game objects retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGameObjects() {
        List<GameResponse> gameObjects = gameObjectService.getAllGameObjects();
        return ResponseEntity.ok(gameObjects);
    }

    /**
     * Deletes a game object by its ID.
     * <p>
     * Deletes the game object corresponding to the given ID. Returns a 204 No Content status if deletion is successful.
     * If the game object is not found, a 404 Not Found status is returned.
     * </p>
     *
     * @param id the ID of the game object to delete.
     * @return a ResponseEntity with no content upon successful deletion.
     */
    @Operation(summary = "Delete Game Object", description = "Deletes a game object by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game object deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Game object not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameObject(@PathVariable(name = "id") Long id) {
        gameObjectService.deleteGameObjectById(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Partially updates a game object.
     * <p>
     * This endpoint accepts a DTO containing only the fields to be updated.
     * Only non-null fields in the provided DTO will be patched into the existing game object.
     * </p>
     *
     * @param id            the ID of the game object to patch.
     * @param gameRequest the DTO with fields to update.
     * @return a ResponseEntity containing the updated GameObjectDto.
     * @throws IllegalAccessException if an error occurs during patching.
     */
    @PatchMapping("/{id}")
    @Operation(
            summary = "Partially update game object",
            description = "Patches the existing game object by updating only non-null fields provided in the DTO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game object patched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Game object not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SneakyThrows
    public ResponseEntity<GameResponse> patchGameObject(@PathVariable(name = "id") Long id,
                                                        @Valid @RequestBody GameRequest gameRequest) {
        GameResponse patchedDto = gameObjectService.patchGameObject(id, gameRequest);
            return ResponseEntity.ok(patchedDto);
    }
}
