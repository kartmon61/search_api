package com.test.search.searchapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.common.dto.response.SearchResult.Blog;
import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import com.test.search.externalapi.common.source.SearchSource;
import com.test.search.externalapi.common.source.SearchSourceFactory;
import com.test.search.externalapi.common.type.SearchType;
import com.test.search.externalapi.kakao.source.KakaoSearchSourceImpl;
import com.test.search.externalapi.naver.source.NaverSearchSourceImpl;
import com.test.search.searchapi.dto.PopularKeywordResponse;
import com.test.search.searchapi.entity.SearchHistory;
import com.test.search.searchapi.entity.SearchKeyword;
import com.test.search.searchapi.repository.SearchHistoryRepository;
import com.test.search.searchapi.repository.SearchKeywordRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlogSearchServiceTest {
    @InjectMocks
    private BlogSearchService blogSearchService;
    @Mock
    private SearchKeywordRepository searchKeywordRepository;
    @Mock
    private SearchHistoryRepository searchHistoryRepository;
    @Mock
    private SearchSourceFactory searchSourceFactory;
    @Mock
    private SearchSource searchSource;
    @Mock
    private KakaoSearchSourceImpl kakaoSearchSource;
    @Mock
    private NaverSearchSourceImpl naverSearchSource;


    private SearchResult searchResult;

    @BeforeEach
    void beforeSetUp() {
        searchResult = SearchResult.builder()
            .content(Arrays.asList(
                Blog.builder()
                    .title("title1")
                    .contents("contents1")
                    .build(),
                Blog.builder()
                    .title("title2")
                    .contents("contents2")
                    .build()))
            .totalElements(2L)
            .build();
    }

    @Test
    @DisplayName("SearchBlogs 결과가 존재할 경우 테스트")
    void testSearchBlogs_whenKeywordExists() {
        // given
        String query = "test";
        String sort = "accuracy";
        Integer page = 1;
        Integer size = 10;
        SearchKeyword searchKeyword = new SearchKeyword(query);

        // Stub
        when(searchSourceFactory.createSearchSource(SearchType.KAKAO)).thenReturn(searchSource);
        when(searchSource.searchBlogs(query, sort, page, size)).thenReturn(searchResult);
        when(searchKeywordRepository.findByKeyword(query)).thenReturn(Optional.of(searchKeyword));

        // when
        blogSearchService.searchBlogs(query, sort, page, size);

        // then
        verify(searchKeywordRepository, times(1)).findByKeyword(query);
        verify(searchKeywordRepository, times(1)).save(searchKeyword);
        verify(searchHistoryRepository, times(1)).save(any(SearchHistory.class));
    }

    @Test
    @DisplayName("SearchBlogs 결과가 존재하지 않을 경우 테스트")
    void testSearchBlogs_whenKeywordNotExists() {
        // given
        String query = "test";
        String sort = "accuracy";
        Integer page = 1;
        Integer size = 10;

        // Stub
        when(searchSourceFactory.createSearchSource(SearchType.KAKAO)).thenReturn(searchSource);
        when(searchSource.searchBlogs(query, sort, page, size)).thenReturn(searchResult);
        when(searchKeywordRepository.findByKeyword(query)).thenReturn(Optional.empty());

        // when
        blogSearchService.searchBlogs(query, sort, page, size);

        // then
        verify(searchKeywordRepository, times(1)).findByKeyword(query);
        verify(searchKeywordRepository, times(1)).save(any(SearchKeyword.class));
        verify(searchHistoryRepository, times(1)).save(any(SearchHistory.class));
    }

    @Test
    @DisplayName("SearchPopularKeyword 키워드가 존재할 때 테스트")
    void testGetPopularKeywordsIfExists() {
        // given
        SearchKeyword keyword1 = new SearchKeyword("test1");
        SearchKeyword keyword2 = new SearchKeyword("test2");
        keyword1.setCount(5L);
        keyword2.setCount(3L);

        // Stub
        when(searchKeywordRepository.findTop10ByOrderByCountDesc()).thenReturn(Arrays.asList(keyword1, keyword2));

        // when
        List<PopularKeywordResponse> popularKeywords = blogSearchService.getPopularKeywords();

        // then
        assertThat(popularKeywords.size()).isEqualTo(2);
        assertThat(popularKeywords.get(0).getKeyword()).isEqualTo("test1");
        assertThat(popularKeywords.get(0).getCount()).isEqualTo(5);
        assertThat(popularKeywords.get(1).getKeyword()).isEqualTo("test2");
        assertThat(popularKeywords.get(1).getCount()).isEqualTo(3);

        verify(searchKeywordRepository, times(1)).findTop10ByOrderByCountDesc();
    }

    @Test
    @DisplayName("SearchPopularKeyword 키워드가 존재하지 않을 때 테스트")
    void testGetPopularKeywordsIfNotExists() {
        // Stub
        when(searchKeywordRepository.findTop10ByOrderByCountDesc()).thenReturn(Collections.emptyList());

        // when
        List<PopularKeywordResponse> popularKeywords = blogSearchService.getPopularKeywords();

        // then
        assertThat(popularKeywords.size()).isEqualTo(0);

        verify(searchKeywordRepository, times(1)).findTop10ByOrderByCountDesc();
    }

    @Test
    @DisplayName("SearchBlogs 로직 중 카카오 api에서 문제가 발생했을 때 네이버 api로 키워드 실행하는 로직 테스트")
    void testSearchBlogsFallbackToNaver() {
        String query = "java";
        String sort = "accuracy";
        Integer page = 1;
        Integer size = 10;

        SearchResult expectedSearchResult = SearchResult.builder()
            .totalPages(1)
            .totalElements(2)
            .content(Arrays.asList(
                Blog.builder()
                    .title("title1")
                    .contents("description1")
                    .build(),
                Blog.builder()
                    .title("title2")
                    .contents("description2")
                    .build()))
            .build();

        when(searchSourceFactory.createSearchSource(SearchType.KAKAO)).thenReturn(kakaoSearchSource);
        when(searchSourceFactory.createSearchSource(SearchType.NAVER)).thenReturn(naverSearchSource);
        when(kakaoSearchSource.searchBlogs(query, sort, page, size)).thenThrow(new ExternalApiException(ExternalApiErrorCode.API_CONNECTION_ERROR));
        when(naverSearchSource.searchBlogs(query, sort, page, size)).thenReturn(expectedSearchResult);

        blogSearchService.searchBlogs(query, sort, page, size);
        verify(searchSourceFactory, times(1)).createSearchSource(SearchType.KAKAO);
        verify(kakaoSearchSource, times(1)).searchBlogs(query, sort, page, size);
        verify(searchSourceFactory, times(1)).createSearchSource(SearchType.NAVER);
        verify(naverSearchSource, times(1)).searchBlogs(query, sort, page, size);
        verify(searchKeywordRepository, times(1)).findByKeyword(query);
        verify(searchKeywordRepository, times(1)).save(any(SearchKeyword.class));
        verify(searchHistoryRepository, times(1)).save(any(SearchHistory.class));
    }

    @Test
    @DisplayName("SearchBlogs 로직 중 카카오 api, 네이버 api 모두 문제가 발생했을 경우 에러를 던짐")
    void testSearchBlogsKakaoAndNaverApiFailed() {
        String query = "java";
        String sort = "accuracy";
        Integer page = 1;
        Integer size = 10;

        when(searchSourceFactory.createSearchSource(SearchType.KAKAO)).thenReturn(kakaoSearchSource);
        when(searchSourceFactory.createSearchSource(SearchType.NAVER)).thenReturn(naverSearchSource);
        when(kakaoSearchSource.searchBlogs(query, sort, page, size)).thenThrow(new ExternalApiException(ExternalApiErrorCode.API_CONNECTION_ERROR));
        when(naverSearchSource.searchBlogs(query, sort, page, size)).thenThrow(new ExternalApiException(ExternalApiErrorCode.API_CONNECTION_ERROR));

        assertThatThrownBy(() -> blogSearchService.searchBlogs(query, sort, page, size))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.API_CONNECTION_ERROR.getMessage());
    }
}