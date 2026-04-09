package com.arsenr.yummy.recipe;

import java.time.Instant;

public record RecipeResponseDto(
        Long id,
        String title,
        String description,
        Integer prepCookTime,
        Integer totalCookTime,
        Integer numService,
        Instant createdAt,
        Instant updatedAt
) {

}
