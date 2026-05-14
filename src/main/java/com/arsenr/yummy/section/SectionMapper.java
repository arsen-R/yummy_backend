package com.arsenr.yummy.section;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SectionMapper {
    SectionMapper INSTANCE = Mappers.getMapper(SectionMapper.class);

    Section sectionRequestDtoToSection(SectionRequestDto sectionRequestDto);
    SectionRequestDto sectionToSectionRequestDto(Section section);

    Section sectionResponseDtoToSection(SectionResponseDto sectionResponseDto);
    SectionResponseDto sectionToSectionResponseDto(Section section);
}
