package com.arsenr.yummy.recipe;

import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

@Builder
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer prepCookTime;
    private Integer cookTime;
    private Integer numService;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getNumService() {
        return numService;
    }

    public void setNumService(Integer numService) {
        this.numService = numService;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponseDto that = (RecipeResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(prepCookTime, that.prepCookTime) &&
                Objects.equals(cookTime, that.cookTime) &&
                Objects.equals(numService, that.numService) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, prepCookTime, cookTime, numService, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "RecipeResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prepCookTime=" + prepCookTime +
                ", totalCookTime=" + cookTime +
                ", numService=" + numService +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
