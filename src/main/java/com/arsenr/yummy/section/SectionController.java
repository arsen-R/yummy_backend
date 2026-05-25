package com.arsenr.yummy.section;

import com.arsenr.yummy.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/section")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<SectionResponseDto>> getAllSections(int page, int size) {
        var response = sectionService.getAllSections(page, size);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionResponseDto> getSectionById(@PathVariable Long sectionId) {
        var response = sectionService.getSectionById(sectionId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSectionById(@PathVariable Long sectionId) {
        sectionService.deleteSectionById(sectionId);
        return ResponseEntity.noContent().build();
    }
}
