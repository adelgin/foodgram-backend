package com.foodgram.foodgrambackend.dto;

import java.util.List;

public class PagedResponseDto<T> {
    private Integer count;
    private String next;
    private String previous;
    private List<T> results;

    public PagedResponseDto() {}

    public PagedResponseDto(Integer count, String next, String previous, List<T> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }

    public String getPrevious() { return previous; }
    public void setPrevious(String previous) { this.previous = previous; }

    public List<T> getResults() { return results; }
    public void setResults(List<T> results) { this.results = results; }
}