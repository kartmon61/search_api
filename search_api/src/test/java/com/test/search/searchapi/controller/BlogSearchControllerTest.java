package com.test.search.searchapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.searchapi.dto.PopularKeywordResponse;
import com.test.search.searchapi.service.BlogSearchService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BlogSearchService blogSearchService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("블로그 검색 api 테스트, 성공")
    void testSearchBlogs() throws Exception {
        String query = "test";
        SearchResult searchResult = new SearchResult();
        when(blogSearchService.searchBlogs(anyString(), any(), any(), any())).thenReturn(searchResult);

        mockMvc.perform(get("/v1/search/blogs")
                .param("query", query)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").exists());

        verify(blogSearchService).searchBlogs(eq(query), any(), any(), any());
    }

    @Test
    @DisplayName("인기 검색어 목록 조회 api 테스트, 성공")
    void testGetPopularKeywords() throws Exception {
        PopularKeywordResponse popularKeywordResponse = PopularKeywordResponse.builder()
            .keyword("test").count(10L).build();
        when(blogSearchService.getPopularKeywords()).thenReturn(Collections.singletonList(popularKeywordResponse));

        mockMvc.perform(get("/v1/search/popular-keywords")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].keyword").value("test"))
            .andExpect(jsonPath("$.data[0].count").value(10));

        verify(blogSearchService).getPopularKeywords();
    }
}