package org.ebndrnk.leverxfinalproject.controller.game;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.game.GamePatchRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameRequest;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.ebndrnk.leverxfinalproject.service.game.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * The controller for handling game object-related requests.
 * <p>
 * This class provides the RESTful API for creating, updating, retrieving, and deleting game objects.
 * It also includes a search functionality and supports pagination for listing game objects.
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
    @PostMapping("")
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
    public ResponseEntity<Page<GameResponse>> getAllGameObjects(Pageable pageable) {
        return ResponseEntity.ok(gameObjectService.getAllGameObjects(pageable));
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
    })
    public ResponseEntity<GameResponse> patchGameObject(@PathVariable(name = "id") Long id,
                                                        @Valid @RequestBody GamePatchRequest gameRequest) {
        GameResponse patchedDto = gameObjectService.patchGameObject(id, gameRequest);
        return ResponseEntity.ok(patchedDto);
    }



    /**
     * Searches game objects based on title or text.
     * <p>
     * Allows searching for game objects by providing one or both of the search criteria:
     * title and text. It returns a paginated list of game objects that match the search parameters.
     * </p>
     *
     * @param title   the title of the game object to search for (optional).
     * @param text    the text content of the game object to search for (optional).
     * @param pageable the pagination information.
     * @return a ResponseEntity containing a paginated list of GameResponse objects matching the search criteria.
     */
    @GetMapping("/search")
    @Operation(summary = "Search Game Objects", description = "Searches game objects by title or text.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game objects retrieved successfully")
    })
    public ResponseEntity<Page<GameResponse>> searchGameObjects(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String text,
            Pageable pageable) {

        return ResponseEntity.ok(gameObjectService.findGameObjects(title, text, pageable));
    }

}
