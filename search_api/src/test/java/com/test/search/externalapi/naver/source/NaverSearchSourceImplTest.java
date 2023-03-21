package com.test.search.externalapi.naver.source;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import com.test.search.externalapi.naver.dto.NaverBlogSearchResponse;
import com.test.search.externalapi.naver.dto.NaverBlogSearchResponse.NaverBlogItem;
import com.test.search.externalapi.naver.validator.NaverBlogSearchValidator;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class NaverSearchSourceImplTest {
    @InjectMocks
    private NaverSearchSourceImpl naverSearchSource;
    @Mock
    private RestTemplate naverRestTemplate;
    @Mock
    private NaverBlogSearchValidator naverBlogSearchValidator;


    @Test
    @DisplayName("블로그 검색 결과 성공")
    void testSearchBlogs() {
        NaverBlogSearchResponse naverBlogSearchResponse = NaverBlogSearchResponse.builder()
            .start(1).total(2).display(2)
            .items(Arrays.asList(
                NaverBlogItem.builder()
                .title("test1")
                .description("description1")
                .build(),
                NaverBlogItem.builder()
                    .title("test2")
                    .description("description2")
                    .build()))
            .build();

        ResponseEntity<NaverBlogSearchResponse> responseEntity = new ResponseEntity<>(
            naverBlogSearchResponse, HttpStatus.OK);

        when(naverRestTemplate.getForEntity(any(String.class), eq(NaverBlogSearchResponse.class))).thenReturn(responseEntity);

        SearchResult searchResult = naverSearchSource.searchBlogs("test", "accuracy", 1, 10);

        assertThat(searchResult.getContent().size()).isEqualTo(2);
        assertThat(searchResult.getContent().get(0).getTitle()).isEqualTo("test1");
        assertThat(searchResult.getContent().get(0).getContents()).isEqualTo("description1");
        assertThat(searchResult.getContent().get(1).getTitle()).isEqualTo("test2");
        assertThat(searchResult.getContent().get(1).getContents()).isEqualTo("description2");
        assertThat(searchResult.getTotalPages()).isEqualTo(1);
        assertThat(searchResult.getTotalElements()).isEqualTo(2);
        verify(naverRestTemplate, times(1)).getForEntity(any(String.class), eq(
            NaverBlogSearchResponse.class));
    }

    @Test
    @DisplayName("블로그 검색 결과, api connection error로 실패")
    void testSearchBlogsApiConnectionErrorException() {
        when(naverRestTemplate.getForEntity(any(String.class), eq(NaverBlogSearchResponse.class)))
            .thenThrow(HttpClientErrorException.class);
        assertThatThrownBy(() -> naverSearchSource.searchBlogs("test", "accuracy", 1, 10))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.API_CONNECTION_ERROR.getMessage());
    }

    @Test
    @DisplayName("블로그 검색 결과, api connection error로 실패")
    void testSearchBlogsUrlInvalidErrorException() {
        when(naverRestTemplate.getForEntity(any(String.class), eq(NaverBlogSearchResponse.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        assertThatThrownBy(() -> naverSearchSource.searchBlogs("test", "accuracy", 1, 10))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.URL_INVALID.getMessage());
    }
}