package com.test.search.externalapi.naver.dto;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.common.dto.response.SearchResult.Blog;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NaverBlogSearchResponse {
    private int total;
    private int start;
    private int display;
    private List<NaverBlogItem> items;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class NaverBlogItem {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private String postdate;

        /**
         * NaverBlogItem에서 Blog로 변환
         * @return Blog
         */
        public Blog toBlog() {
            return Blog.builder()
                .title(title)
                .contents(description)
                .url(bloggername)
                .cafename(bloggerlink)
                .datetime(postdate)
                .build();
        }
    }

    /**
     * NaverBlogSearchResponse에서 SearchResult로 변환
     * @param size
     * @param page
     * @return SearchResult
     */
    public SearchResult toSearchResult(int size, int page) {
        return SearchResult.builder()
            .size(size)
            .page(page)
            .totalElements(total)
            .totalPages(total/display)
            .content(items.stream()
                .map(document -> document.toBlog())
                .collect(Collectors.toList()))
            .build();
    }
}
