package com.arsenr.yummy.recipe;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RecipeRequestDto(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        Integer prepCookTime,
        Integer totalCookTime,
        @Min(value = 1, message = "Servings must be at least 1")
        Integer numService
) {
}
