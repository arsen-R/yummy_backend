package com.arsenr.yummy.section;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    Section sectionRequestDtoToSection(SectionRequestDto sectionRequestDto);
    SectionRequestDto sectionToSectionRequestDto(Section section);

    Section sectionResponseDtoToSection(SectionResponseDto sectionResponseDto);
    SectionResponseDto sectionToSectionResponseDto(Section section);
}
