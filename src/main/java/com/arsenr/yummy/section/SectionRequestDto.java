package com.arsenr.yummy.section;

import java.util.Objects;

public class SectionRequestDto {
    private String sectionName;
    private Integer position;

    public SectionRequestDto() {}

    public SectionRequestDto(String sectionName, Integer position) {
        this.sectionName = sectionName;
        this.position = position;
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
        SectionRequestDto that = (SectionRequestDto) o;
        return Objects.equals(sectionName, that.sectionName) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionName, position);
    }

    @Override
    public String toString() {
        return "SectionRequestDto{" +
                "sectionName='" + sectionName + '\'' +
                ", position=" + position +
                '}';
    }
}
