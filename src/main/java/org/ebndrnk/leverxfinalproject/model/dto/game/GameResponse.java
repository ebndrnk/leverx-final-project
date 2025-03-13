package org.ebndrnk.leverxfinalproject.model.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GameResponse {

    @Schema(description = "ID of the game", example = "1")
    private Long id;

    @Schema(description = "Title of the game", example = "Epic Adventure")
    private String title;

    @Schema(description = "Text description of the game", example = "An exciting adventure game with multiple levels.")
    private String text;
}
