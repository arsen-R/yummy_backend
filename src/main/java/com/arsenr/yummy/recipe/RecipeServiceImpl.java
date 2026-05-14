package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import com.arsenr.yummy.user.User;
import com.arsenr.yummy.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final UserRepository userRepository;

    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            RecipeMapper recipeMapper,
            UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.userRepository = userRepository;
    }


    @Override
    public PageResponse<RecipeResponseDto> getAllRecipes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);
        List<RecipeResponseDto> recipeResponse = recipePage.stream()
                .map(recipeMapper::recipeToRecipeResponseDto)
                .toList();
        return PageResponse.<RecipeResponseDto>builder()
                .content(recipeResponse)
                .number(recipePage.getNumber())
                .size(recipePage.getSize())
                .totalElements(recipePage.getTotalElements())
                .totalPages(recipePage.getTotalPages())
                .first(recipePage.isFirst())
                .last(recipePage.isLast())
                .build();
    }

    @Override
    public RecipeResponseDto getRecipeById(Long recipeId) {
        return recipeRepository.findRecipeById(recipeId)
                .map(recipeMapper::recipeToRecipeResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));
    }

    @Override
    public RecipeResponseDto saveRecipe(RecipeRequestDto recipeRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));
        Recipe recipe = recipeMapper.recipeRequestDtoToRecipe(recipeRequestDto);
        recipe.setOwner(user);
        return recipeMapper.recipeToRecipeResponseDto(recipeRepository.save(recipe));
    }

    @Override
    public RecipeResponseDto updateRecipe(Long recipeId, RecipeRequestDto recipeRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));

        if (!Objects.equals(recipe.getOwner().getEmail(), user.getEmail())) {
            throw new AccessDeniedException("You do not own this recipe");
        }

        recipe.setTitle(recipeRequestDto.getTitle());
        recipe.setDescription(recipeRequestDto.getDescription());
        recipe.setNumService(recipeRequestDto.getNumService());
        recipe.setPrepCookTime(recipeRequestDto.getPrepCookTime());
        recipe.setCookTime(recipeRequestDto.getTotalCookTime());

        return recipeMapper.recipeToRecipeResponseDto(recipeRepository.save(recipe));
    }

    @Override
    public void deleteRecipeById(Long recipeId, UserDetails userDetails) {
        recipeRepository.delete(recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId)));
    }
}
