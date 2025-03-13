package org.ebndrnk.leverxfinalproject.model.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RatingRequest {
    @Min(value = 1, message = "mark should be greater than or equal to 1")
    @Max(value = 10, message = "mark should be less than or equal to 10")
    @Schema(description = "mark")
    private byte mark;
}
