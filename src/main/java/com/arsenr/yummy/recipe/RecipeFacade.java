package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import com.arsenr.yummy.section.SectionResponseDto;
import com.arsenr.yummy.section.SectionService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeFacade {
    private final RecipeService recipeService;
    private final SectionService sectionService;

    public RecipeFacade(RecipeService recipeService, SectionService sectionService) {
        this.recipeService = recipeService;
        this.sectionService = sectionService;
    }

    @Transactional
    public FullRecipeResponseDto createFullRecipe(FullRecipeRequestDto fullRecipeRequestDto, UserDetails userDetails) {
        RecipeResponseDto savedRecipe = recipeService.saveRecipe(fullRecipeRequestDto.getRecipe(), userDetails);

        List<SectionResponseDto> savedSections = List.of();
        if (fullRecipeRequestDto.getSections() != null && !fullRecipeRequestDto.getSections().isEmpty()) {
            savedSections = sectionService.createSectionsByRecipeId(savedRecipe.getId(), fullRecipeRequestDto.getSections());
        }

        return FullRecipeResponseDto.builder()
                .recipe(savedRecipe)
                .sections(savedSections)
                .build();
    }

    @Transactional
    public FullRecipeResponseDto updateFullRecipe(Long recipeId, FullRecipeRequestDto fullRecipeRequestDto, UserDetails userDetails) {
        RecipeResponseDto updatedRecipe = recipeService.updateRecipe(recipeId, fullRecipeRequestDto.getRecipe(), userDetails);

        List<SectionResponseDto> existingSections = sectionService.getSectionByRecipeId(recipeId);
        for (SectionResponseDto existingSection : existingSections) {
            sectionService.deleteSectionById(existingSection.getId());
        }

        List<SectionResponseDto> updatedSections = new ArrayList<>();
        if (fullRecipeRequestDto.getSections() != null && !fullRecipeRequestDto.getSections().isEmpty()) {
            updatedSections = sectionService.createSectionsByRecipeId(recipeId, fullRecipeRequestDto.getSections());
        }

        return FullRecipeResponseDto.builder()
                .recipe(updatedRecipe)
                .sections(updatedSections)
                .build();
    }


    public PageResponse<RecipeResponseDto> getAllRecipes(int page, int size) {
        return recipeService.getAllRecipes(page, size);
    }

    public RecipeResponseDto getRecipeById(Long recipeId) {
        return recipeService.getRecipeById(recipeId);
    }

    @Transactional
    public void deleteRecipeById(Long recipeId, UserDetails userDetails) {
        recipeService.deleteRecipeById(recipeId, userDetails);
    }

    public List<SectionResponseDto> getSectionByRecipeId(Long recipeId) {
        return sectionService.getSectionByRecipeId(recipeId);
    }
}
