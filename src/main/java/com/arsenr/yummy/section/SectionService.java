package com.arsenr.yummy.section;

import com.arsenr.yummy.common.PageResponse;

import java.util.List;

public interface SectionService {
    PageResponse<List<Section>> getAllSections(int page, int size);

    SectionResponseDto getSectionById(Long sectionId);

    SectionResponseDto createSectionByRecipeId(Long recipeId, SectionRequestDto sectionRequestDto);

    SectionResponseDto updateSectionById(Long recipeId, SectionRequestDto sectionRequestDto);

    SectionResponseDto deleteSectionById(Long recipeId);
}
