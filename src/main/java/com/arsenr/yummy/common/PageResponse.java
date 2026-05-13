package com.arsenr.yummy.common;

import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
public class PageResponse<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;


    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PageResponse<?> that = (PageResponse<?>) o;
        return number == that.number && size == that.size && totalElements == that.totalElements && totalPages == that.totalPages && first == that.first && last == that.last && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, number, size, totalElements, totalPages, first, last);
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "content=" + content +
                ", number=" + number +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", first=" + first +
                ", last=" + last +
                '}';
    }
}
