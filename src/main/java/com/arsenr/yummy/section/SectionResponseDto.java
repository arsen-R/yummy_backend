package com.arsenr.yummy.section;

import lombok.Builder;

import java.util.Objects;

@Builder
public class SectionResponseDto {
    private Long id;
    private String sectionName;
    private Integer position;


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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SectionResponseDto that = (SectionResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(sectionName, that.sectionName) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectionName, position);
    }

    @Override
    public String toString() {
        return "SectionResponseDto{" +
                "id=" + id +
                ", sectionName='" + sectionName + '\'' +
                ", position=" + position +
                '}';
    }
}
