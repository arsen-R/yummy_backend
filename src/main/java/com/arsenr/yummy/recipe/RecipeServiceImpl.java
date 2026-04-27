package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import com.arsenr.yummy.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }


    @Override
    public PageResponse<RecipeResponseDto> getAllRecipes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);
        List<RecipeResponseDto> recipeResponse = recipePage.stream()
                .map(recipeMapper::recipeToRecipeResponseDto)
                .toList();
        return new PageResponse<>(
                recipeResponse,
                recipePage.getNumber(),
                recipePage.getSize(),
                recipePage.getTotalElements(),
                recipePage.getTotalPages(),
                recipePage.isFirst(),
                recipePage.isLast()
        );
    }

    @Override
    public RecipeResponseDto getRecipeById(Long recipeId) {
        return recipeRepository.findRecipeByRecipeId(recipeId)
                .map(recipeMapper::recipeToRecipeResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));
    }

    @Override
    public RecipeResponseDto saveRecipe(RecipeRequestDto recipeRequestDto, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Recipe recipe = recipeMapper.recipeRequestDtoToRecipe(recipeRequestDto);
        recipe.setOwner(user);
        return recipeMapper.recipeToRecipeResponseDto(recipeRepository.save(recipe));
    }

    @Override
    public RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto, Authentication connectedUser) {

        Recipe recipe = recipeRepository.findRecipeByRecipeId(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));

        if (!Objects.equals(recipe.getCreatedBy(), connectedUser.getName())) {
            throw new AccessDeniedException("You do not own this recipe");
        }

        recipe.setTitle(recipeRequestDto.title());
        recipe.setDescription(recipeRequestDto.description());
        recipe.setNumService(recipeRequestDto.numService());
        recipe.setPrepCookTime(recipeRequestDto.prepCookTime());
        recipe.setTotalCookTime(recipeRequestDto.totalCookTime());

        return recipeMapper.recipeToRecipeResponseDto(recipeRepository.save(recipe));
    }

    @Override
    public void deleteRecipeById(Long recipeId) {
        recipeRepository.delete(recipeRepository.findRecipeByRecipeId(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId)));
    }
}
