package com.test.search.externalapi.naver.source;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import com.test.search.externalapi.naver.dto.NaverBlogSearchResponse;
import com.test.search.externalapi.common.source.SearchSource;
import com.test.search.externalapi.naver.validator.NaverBlogSearchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverSearchSourceImpl implements SearchSource {

    private final RestTemplate naverRestTemplate;
    private final NaverBlogSearchValidator naverBlogSearchValidator;
    private static final String NAVER_BLOG_SEARCH_API_URL = "https://openapi.naver.com/v1/search/blog";

    /**
     * 네이버 블로그 검색 API (검색, 정렬, 페이징, 조회 개수)
     * TODO: 동일한 로직 반복되기에 추후 핵심 로직과 부가 로직으로 분리
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return BlogSearchResponse
     */
    @Override
    public SearchResult searchBlogs(String query, String sort, Integer page, Integer size) {
        try {
            ResponseEntity<NaverBlogSearchResponse> response =
                naverRestTemplate.getForEntity(createUrl(query, sort, page, size), NaverBlogSearchResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().toSearchResult(
                    size != null ? size : 10,
                    page != null ? page : 1);
            } else {
                throw new ExternalApiException(ExternalApiErrorCode.URL_INVALID);
            }
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException(ExternalApiErrorCode.API_CONNECTION_ERROR);
        }
    }

    @Override
    public String getSourceName() {
        return "naver";
    }

    /**
     * 네이버 블로그 검색 API URL 검증 및 생성
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return String
     */
    private String createUrl(String query, String sort, Integer page, Integer size) {
        String url = NAVER_BLOG_SEARCH_API_URL + "?query=" + query;

        if (sort != null) {
            url += "&sort=" + naverBlogSearchValidator.validateSort(sort);
        }
        if (page != null) {
            url += "&start=" + naverBlogSearchValidator.validatePage(page);
        }
        if (size != null) {
            url += "&display=" + naverBlogSearchValidator.validateSize(size);
        }

        return url;
    }
}
