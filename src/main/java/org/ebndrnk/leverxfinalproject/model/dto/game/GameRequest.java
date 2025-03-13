package org.ebndrnk.leverxfinalproject.model.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GameRequest {

    @Schema(description = "Text description of the game", example = "An exciting adventure game with multiple levels.")
    @NotBlank(message = "Text cannot be blank.")
    @Size(max = 1000, message = "Text cannot exceed 1000 characters.")
    private String text;

    @Schema(description = "Title of the game", example = "Epic Adventure")
    @NotBlank(message = "Title cannot be blank.")
    @Size(max = 255, message = "Title cannot exceed 255 characters.")
    private String title;
}
