package com.test.search.externalapi.common.source;


import com.test.search.common.dto.response.SearchResult;

public interface SearchSource {

    /**
     * 블로그 검색 기능 (검색, 정렬, 페이징, 조회 개수)
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return SearchResult
     */
     SearchResult searchBlogs(String query, String sort, Integer page, Integer size);

    /**
     * 블로그 검색에 사용된 api 소스명
     * @return String
     */
    String getSourceName();
}
