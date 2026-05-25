package com.arsenr.yummy.section;

import com.arsenr.yummy.common.PageResponse;
import com.arsenr.yummy.recipe.Recipe;
import com.arsenr.yummy.recipe.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final RecipeRepository recipeRepository;
    private final SectionMapper sectionMapper;

    public SectionServiceImpl(SectionRepository sectionRepository,
                              RecipeRepository recipeRepository,
                              SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.recipeRepository = recipeRepository;
        this.sectionMapper = sectionMapper;
    }

    @Override
    public PageResponse<SectionResponseDto> getAllSections(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Section> sectionPage = sectionRepository.findAll(pageRequest);
        List<SectionResponseDto> sectionResponse = sectionPage.stream().map(sectionMapper::sectionToSectionResponseDto).toList();
        return PageResponse.<SectionResponseDto>builder()
                .content(sectionResponse)
                .number(sectionPage.getNumber())
                .size(sectionPage.getSize())
                .totalElements(sectionPage.getTotalElements())
                .totalPages(sectionPage.getTotalPages())
                .first(sectionPage.isFirst())
                .last(sectionPage.isLast())
                .build();
    }

    @Override
    public List<SectionResponseDto> getSectionByRecipeId(Long recipeId) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));
        List<Section> sections = recipe.getSections();
        return sections.stream().map(sectionMapper::sectionToSectionResponseDto).toList();
    }

    @Override
    public SectionResponseDto getSectionById(Long sectionId) {
        return sectionRepository.findSectionById(sectionId)
                .map(sectionMapper::sectionToSectionResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("No section found with id: " + sectionId));
    }

    @Override
    @Transactional
    public List<SectionResponseDto> createSectionsByRecipeId(Long recipeId, List<SectionRequestDto> sectionRequestDto) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));

        if (sectionRequestDto == null || sectionRequestDto.isEmpty()) {
            throw new IllegalArgumentException("Section Request cannot be null or empty");
        }

        List<Section> savedSections = sectionRepository.saveAll(sectionRequestDto.stream()
                .map(dto -> {
                    Section section = sectionMapper.sectionRequestDtoToSection(dto);
                    recipe.addSection(section);
                    return section;
                }).toList());

        return savedSections.stream().map(sectionMapper::sectionToSectionResponseDto).toList();
    }

    @Override
    @Transactional
    public SectionResponseDto createSectionByRecipeId(Long recipeId, SectionRequestDto sectionRequestDto) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));
        if (sectionRequestDto == null) {
            throw new IllegalArgumentException("Section Request cannot be null or empty");
        }
        Section section = sectionMapper.sectionRequestDtoToSection(sectionRequestDto);
        recipe.addSection(section);

        return sectionMapper.sectionToSectionResponseDto(sectionRepository.save(section));
    }

    @Override
    @Transactional
    public SectionResponseDto updateSectionById(Long recipeId, Long sectionId, SectionRequestDto sectionRequestDto) {
//        Recipe recipe = recipeRepository.findRecipeById(recipeId)
//                .orElseThrow(() -> new EntityNotFoundException("No recipe found with id: " + recipeId));

        Section existingSection = sectionRepository.findSectionById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("No section found with id: " + sectionId));

        if (!existingSection.getRecipe().getId().equals(recipeId)) {
            throw new IllegalArgumentException("Section does not belong to the specified recipe");
        }

        if (sectionRequestDto == null) {
            throw new IllegalArgumentException("Section Request cannot be null or empty");
        }
        existingSection.setSectionName(sectionRequestDto.getSectionName());
        existingSection.setPosition(sectionRequestDto.getPosition());

        Section updatedSection = sectionRepository.save(existingSection);

        return sectionMapper.sectionToSectionResponseDto(updatedSection);
    }

    @Override
    @Transactional
    public void deleteSectionById(Long sectionId) {
        sectionRepository.delete(sectionRepository.findSectionById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("No section found with id: " + sectionId)));
    }
}
