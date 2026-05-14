package com.arsenr.yummy.recipe;

import com.arsenr.yummy.user.UserDto;
import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

@Builder
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer prepCookTime;
    private Integer totalCookTime;
    private Integer numService;
    private Instant createdAt;
    private Instant updatedAt;
    private UserDto createdBy;

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

    public UserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDto createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponseDto that = (RecipeResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(prepCookTime, that.prepCookTime) && Objects.equals(totalCookTime, that.totalCookTime) && Objects.equals(numService, that.numService) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, prepCookTime, totalCookTime, numService, createdAt, updatedAt, createdBy);
    }

    @Override
    public String toString() {
        return "RecipeResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prepCookTime=" + prepCookTime +
                ", totalCookTime=" + totalCookTime +
                ", numService=" + numService +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy=" + createdBy +
                '}';
    }
}
