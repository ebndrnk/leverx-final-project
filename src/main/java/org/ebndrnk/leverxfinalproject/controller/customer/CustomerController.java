package org.ebndrnk.leverxfinalproject.controller.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.game.GameResponse;
import org.ebndrnk.leverxfinalproject.model.entity.game.GameObject;
import org.ebndrnk.leverxfinalproject.service.game.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final GameService gameService;

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
    @GetMapping("/game")
    public ResponseEntity<List<GameResponse>> getAllGameObjects() {
        List<GameResponse> gameObjects = gameService.getAllGameObjects();
        return ResponseEntity.ok(gameObjects);
    }
}
