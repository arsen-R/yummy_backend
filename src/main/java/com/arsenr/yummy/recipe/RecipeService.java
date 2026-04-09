package com.arsenr.yummy.recipe;

import java.util.List;

public interface RecipeService {
    List<RecipeResponseDto> getAllRecipes();
    RecipeResponseDto getRecipeById(Long recipeId);

    RecipeResponseDto saveRecipe(RecipeRequestDto recipe);
    RecipeResponseDto updateRecipe(RecipeRequestDto recipe);
    void deleteRecipeById(Long recipeId);
}
