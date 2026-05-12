package com.arsenr.yummy.recipe;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    Recipe recipeRequestDtoToRecipe(RecipeRequestDto recipeRequestDto);
    RecipeRequestDto recipeToRecipeRequestDto(Recipe recipe);

    RecipeResponseDto recipeToRecipeResponseDto(Recipe recipe);
    Recipe recipeResponseDtoToRecipe(RecipeResponseDto recipeResponseDto);
}
