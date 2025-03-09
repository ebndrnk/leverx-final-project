package org.ebndrnk.leverxfinalproject.model.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GameDto {
    private Long id;
    @Schema(description = "title of the game")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Schema(description = "Game description")
    private String text;
}
