package com.arsenr.yummy.section;

import com.arsenr.yummy.common.PageResponse;

import java.util.List;

public interface SectionService {
    PageResponse<SectionResponseDto> getAllSections(int page, int size);

    List<SectionResponseDto> getSectionByRecipeId(Long recipeId);

    SectionResponseDto getSectionById(Long sectionId);

    List<SectionResponseDto> createSectionsByRecipeId(Long recipeId, List<SectionRequestDto> sectionRequestDto);

    SectionResponseDto createSectionByRecipeId(Long recipeId, SectionRequestDto sectionRequestDto);

    SectionResponseDto updateSectionById(Long recipeId, Long sectionId, SectionRequestDto sectionRequestDto);

    void deleteSectionById(Long sectionId);
}
