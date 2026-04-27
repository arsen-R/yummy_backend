package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RecipeService {
    PageResponse<RecipeResponseDto> getAllRecipes(int page, int size);
    RecipeResponseDto getRecipeById(Long recipeId);

    RecipeResponseDto saveRecipe(RecipeRequestDto recipeRequestDto, Authentication connectedUser);
    RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto, Authentication connectedUser);
    void deleteRecipeById(Long recipeId);
}
