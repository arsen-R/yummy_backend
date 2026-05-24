package com.arsenr.yummy.recipe;

import com.arsenr.yummy.common.PageResponse;
import com.arsenr.yummy.section.SectionResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<RecipeResponseDto>> getAllRecipes(@RequestParam(required = true, name = "page") Integer page,
                                                                         @RequestParam(required = true, name = "size") Integer size) {
        var result = recipeService.getAllRecipes(page, size);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipeId(@Valid @PathVariable Long recipeId) {
        var result = recipeService.getRecipeById(recipeId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<RecipeResponseDto> saveRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        var result = recipeService.saveRecipe(recipeRequestDto, userDetails);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{recipeId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<RecipeResponseDto> updateRecipe(@Valid @PathVariable Long recipeId,
                                                          @Valid @RequestBody RecipeRequestDto recipeRequestDto,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        var result = recipeService.updateRecipe(recipeId, recipeRequestDto, userDetails);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{recipeId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteRecipe(@Valid @PathVariable Long recipeId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.deleteRecipeById(recipeId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{recipeId}/sections")
    public ResponseEntity<List<SectionResponseDto>> getSectionsByRecipeId(@Valid @PathVariable Long recipeId) {
        var result = recipeService.getSectionByRecipeId(recipeId);
        return ResponseEntity.ok().body(result);
    }
}
