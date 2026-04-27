package com.arsenr.yummy.recipe;

import com.arsenr.yummy.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @Column(length = 250)
    private String description;
    private Integer prepCookTime;
    private Integer totalCookTime;
    private Integer numService;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @Column(nullable = false)
    private String createdBy;

    public Recipe() {
    }

    public Recipe(Long id, String title, String description, Integer totalCookTime, Integer numService, Instant createdAt, Instant updatedAt, User owner, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.totalCookTime = totalCookTime;
        this.numService = numService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
        this.createdBy = createdBy;
    }

    public Recipe(String title, String description, Integer totalCookTime, Integer numService, Instant createdAt, Instant updatedAt, User owner, String createdBy) {
        this.title = title;
        this.description = description;
        this.totalCookTime = totalCookTime;
        this.numService = numService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
        this.createdBy = createdBy;
    }

    public Recipe(String title, String description, Integer totalCookTime, Integer numService, User owner, String createdBy) {
        this.title = title;
        this.description = description;
        this.totalCookTime = totalCookTime;
        this.numService = numService;
        this.owner = owner;
        this.createdBy = createdBy;
    }

    public Recipe(String title, String description, Integer totalCookTime, Integer numService, Instant createdAt, Instant updatedAt) {
        this.title = title;
        this.description = description;
        this.totalCookTime = totalCookTime;
        this.numService = numService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Recipe(String title, String description, Integer totalCookTime, Integer numService) {
        this.title = title;
        this.description = description;
        this.totalCookTime = totalCookTime;
        this.numService = numService;
    }

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

    public Integer getTotalTimeMinutes() {
        return totalCookTime;
    }

    public void setTotalTimeMinutes(Integer totalTimeMinutes) {
        this.totalCookTime = totalTimeMinutes;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id)
                && Objects.equals(title, recipe.title)
                && Objects.equals(description, recipe.description)
                && Objects.equals(totalCookTime, recipe.totalCookTime)
                && Objects.equals(numService, recipe.numService)
                && Objects.equals(createdAt, recipe.createdAt)
                && Objects.equals(updatedAt, recipe.updatedAt)
                && Objects.equals(owner, recipe.owner)
                && Objects.equals(createdBy, recipe.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, totalCookTime, numService, createdAt, updatedAt, owner, createdBy);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", totalTimeMinutes=" + totalCookTime +
                ", numService=" + numService +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", owner=" + owner +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
