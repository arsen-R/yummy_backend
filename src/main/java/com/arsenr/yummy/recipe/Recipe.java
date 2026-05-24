package com.arsenr.yummy.recipe;

import com.arsenr.yummy.section.Section;
import com.arsenr.yummy.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false)
    private Integer prepCookTime;
    @Column(nullable = false)
    private Integer cookTime;
    @Column(nullable = false)
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
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(Long id,
                  String title,
                  String description,
                  Integer prepCookTime,
                  Integer cookTime,
                  Integer numService,
                  Instant createdAt,
                  Instant updatedAt,
                  User owner,
                  List<Section> sections) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.prepCookTime = prepCookTime;
        this.cookTime = cookTime;
        this.numService = numService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
        this.sections = sections;
    }

    public Recipe(String title,
                  String description,
                  Integer prepCookTime,
                  Integer cookTime,
                  Integer numService,
                  Instant createdAt,
                  Instant updatedAt,
                  User owner,
                  List<Section> sections) {
        this.title = title;
        this.description = description;
        this.prepCookTime = prepCookTime;
        this.cookTime = cookTime;
        this.numService = numService;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
        this.sections = sections;
    }

    public Recipe(String title,
                  String description,
                  Integer prepCookTime,
                  Integer cookTime,
                  Integer numService,
                  User owner,
                  List<Section> sections) {
        this.title = title;
        this.description = description;
        this.prepCookTime = prepCookTime;
        this.cookTime = cookTime;
        this.numService = numService;
        this.owner = owner;
        this.sections = sections;
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

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        sections.add(section);
        section.setRecipe(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setRecipe(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) &&
                Objects.equals(title, recipe.title) &&
                Objects.equals(description, recipe.description) &&
                Objects.equals(prepCookTime, recipe.prepCookTime) &&
                Objects.equals(cookTime, recipe.cookTime) &&
                Objects.equals(numService, recipe.numService) &&
                Objects.equals(createdAt, recipe.createdAt) &&
                Objects.equals(updatedAt, recipe.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, prepCookTime, cookTime, numService, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prepCookTime=" + prepCookTime +
                ", cookTime=" + cookTime +
                ", numService=" + numService +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
