package com.arsenr.yummy.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findSectionById(Long id);
    Boolean existsSectionBySectionName(String sectionName);
}
