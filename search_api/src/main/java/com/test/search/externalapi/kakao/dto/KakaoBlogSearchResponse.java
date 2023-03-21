package com.test.search.externalapi.kakao.dto;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.common.dto.response.SearchResult.Blog;
import java.util.ArrayList;
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
public class KakaoBlogSearchResponse {
    private Meta meta;
    private List<Document> documents = new ArrayList<>();

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Document {
        private String title;
        private String contents;
        private String url;
        private String cafename;

        private String thumbnail;
        private String datetime;

        /**
         * Document에서 Blog로 변환
         * @return Blog
         */
        public Blog toBlog() {
            return Blog.builder()
                  .title(title)
                  .contents(contents)
                  .url(url)
                  .cafename(cafename)
                  .datetime(datetime)
                  .thumbnail(thumbnail)
                  .build();
        }
    }

    /**
     * KakaoBlogSearchResponse에서 SearchResult로 변환
     * @param size
     * @param page
     * @return SearchResult
     */
    public SearchResult toSearchResult(int size, int page) {
        return SearchResult.builder()
                .size(size)
                .page(page)
                .totalElements(meta.getTotal_count())
                .totalPages(meta.getPageable_count())
                .content(documents.stream()
                    .map(document -> document.toBlog())
                    .collect(Collectors.toList()))
                .build();
    }
}
