package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import com.arsenr.yummy.section.SectionResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface RecipeService {
    PageResponse<RecipeResponseDto> getAllRecipes(int page, int size);
    RecipeResponseDto getRecipeById(Long recipeId);

    RecipeResponseDto saveRecipe(RecipeRequestDto recipe, UserDetails userDetails);
    RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipe, UserDetails userDetails);
    void deleteRecipeById(Long recipeId, UserDetails userDetails);

    List<SectionResponseDto> getSectionByRecipeId(Long recipeId);
}
