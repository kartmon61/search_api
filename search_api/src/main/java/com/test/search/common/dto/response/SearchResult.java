package com.test.search.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@JsonInclude(Include.NON_NULL)
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class SearchResult {
    private List<Blog> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    @JsonInclude(Include.NON_NULL)
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Blog {
        private String title;
        private String url;
        private String contents;
        private String cafename;
        private String thumbnail;
        private String datetime;
    }
}
