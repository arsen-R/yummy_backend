package com.arsenr.yummy.recipe;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    Recipe recipeRequestDtoToRecipe(RecipeRequestDto recipeRequestDto);
    RecipeRequestDto recipeToRecipeRequestDto(Recipe recipe);

    RecipeResponseDto recipeToRecipeResponseDto(Recipe recipe);
    Recipe recipeResponseDtoToRecipe(RecipeResponseDto recipeResponseDto);
}
