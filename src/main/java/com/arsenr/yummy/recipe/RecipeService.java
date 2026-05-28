package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface RecipeService {
    PageResponse<RecipeResponseDto> getAllRecipes(int page, int size);
    RecipeResponseDto getRecipeById(Long recipeId);

    RecipeResponseDto saveRecipe(RecipeRequestDto recipe, UserDetails userDetails);
    RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipe, UserDetails userDetails);
    void deleteRecipeById(Long recipeId, UserDetails userDetails);
}
