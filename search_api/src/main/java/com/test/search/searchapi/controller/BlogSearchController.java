package com.test.search.searchapi.controller;

import com.test.search.common.dto.response.BaseResponse;
import com.test.search.common.dto.response.SearchResult;
import com.test.search.searchapi.dto.PopularKeywordResponse;
import com.test.search.searchapi.service.BlogSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/search")
public class BlogSearchController {
    private final BlogSearchService blogSearchService;

    /**
     * 블로그 검색 API
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return BlogSearchResponse
     */
    @GetMapping("/blogs")
    public BaseResponse<SearchResult> searchBlogs(
        @RequestParam String query,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }
        SearchResult response = blogSearchService.searchBlogs(query, sort, page,
            size);

        return BaseResponse.ok(response);
    }

    /**
     * 상위 10개 인기검색어 정보 API
     * @return List<PopularKeywordResponse>
     */
    @GetMapping("/popular-keywords")
    public BaseResponse<List<PopularKeywordResponse>> getPopularKeywords() {
        List<PopularKeywordResponse> response = blogSearchService.getPopularKeywords();

        return BaseResponse.ok(response);
    }
}
