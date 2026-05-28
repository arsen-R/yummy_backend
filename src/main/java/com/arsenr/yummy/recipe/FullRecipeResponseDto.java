package com.arsenr.yummy.recipe;

import com.arsenr.yummy.section.SectionResponseDto;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
public class FullRecipeResponseDto {
    private RecipeResponseDto recipe;
    private List<SectionResponseDto> sections;

    public RecipeResponseDto getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeResponseDto recipe) {
        this.recipe = recipe;
    }

    public List<SectionResponseDto> getSections() {
        return sections;
    }

    public void setSections(List<SectionResponseDto> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FullRecipeResponseDto that = (FullRecipeResponseDto) o;
        return Objects.equals(recipe, that.recipe) && Objects.equals(sections, that.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, sections);
    }

    @Override
    public String toString() {
        return "FullRecipeRequestDto{" +
                "recipe=" + recipe +
                ", sections=" + sections +
                '}';
    }
}
