package com.arsenr.yummy.section;

import com.arsenr.yummy.recipe.Recipe;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sectionName;
    private Integer position;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public Section() {}

    public Section(String sectionName, Integer position) {
        this.sectionName = sectionName;
        this.position = position;
    }

    public Section(Long id, String sectionName, Integer position) {
        this.id = id;
        this.sectionName = sectionName;
        this.position = position;
    }

    public Section(String sectionName, Integer position, Recipe recipe) {
        this.sectionName = sectionName;
        this.position = position;
        this.recipe = recipe;
    }

    public Section(Long id, String sectionName, Integer position, Recipe recipe) {
        this.id = id;
        this.sectionName = sectionName;
        this.position = position;
        this.recipe = recipe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(sectionName, section.sectionName) && Objects.equals(position, section.position) && Objects.equals(createdAt, section.createdAt) && Objects.equals(updatedAt, section.updatedAt) && Objects.equals(recipe, section.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectionName, position, createdAt, updatedAt, recipe);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", sectionName='" + sectionName + '\'' +
                ", position=" + position +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", recipe=" + recipe +
                '}';
    }
}
