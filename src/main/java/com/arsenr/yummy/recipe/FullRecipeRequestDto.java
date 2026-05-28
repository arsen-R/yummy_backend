package com.arsenr.yummy.recipe;

import com.arsenr.yummy.section.SectionRequestDto;

import java.util.List;
import java.util.Objects;

public class FullRecipeRequestDto {
    private RecipeRequestDto recipe;
    private List<SectionRequestDto> sections;

    public FullRecipeRequestDto() {
    }

    public FullRecipeRequestDto(RecipeRequestDto recipe, List<SectionRequestDto> sections) {
        this.recipe = recipe;
        this.sections = sections;
    }

    public RecipeRequestDto getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeRequestDto recipe) {
        this.recipe = recipe;
    }

    public List<SectionRequestDto> getSections() {
        return sections;
    }

    public void setSections(List<SectionRequestDto> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FullRecipeRequestDto that = (FullRecipeRequestDto) o;
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
