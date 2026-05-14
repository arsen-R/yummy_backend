package com.arsenr.yummy.recipe;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Objects;

public class RecipeRequestDto {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private Integer prepCookTime;
    private Integer totalCookTime;
    @Min(value = 1, message = "Servings must be at least 1")
    private Integer numService;

    public RecipeRequestDto() {}

    public RecipeRequestDto(String title, String description, Integer prepCookTime, Integer totalCookTime, Integer numService) {
        this.title = title;
        this.description = description;
        this.prepCookTime = prepCookTime;
        this.totalCookTime = totalCookTime;
        this.numService = numService;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrepCookTime() {
        return prepCookTime;
    }

    public void setPrepCookTime(Integer prepCookTime) {
        this.prepCookTime = prepCookTime;
    }

    public Integer getTotalCookTime() {
        return totalCookTime;
    }

    public void setTotalCookTime(Integer totalCookTime) {
        this.totalCookTime = totalCookTime;
    }

    public Integer getNumService() {
        return numService;
    }

    public void setNumService(Integer numService) {
        this.numService = numService;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRequestDto that = (RecipeRequestDto) o;
        return Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(prepCookTime, that.prepCookTime) && Objects.equals(totalCookTime, that.totalCookTime) && Objects.equals(numService, that.numService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, prepCookTime, totalCookTime, numService);
    }

    @Override
    public String toString() {
        return "RecipeRequestDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prepCookTime=" + prepCookTime +
                ", totalCookTime=" + totalCookTime +
                ", numService=" + numService +
                '}';
    }
}
