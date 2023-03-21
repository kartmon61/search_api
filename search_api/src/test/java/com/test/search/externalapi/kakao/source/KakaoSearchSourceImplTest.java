package com.test.search.externalapi.kakao.source;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.search.common.dto.response.SearchResult;
import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import com.test.search.externalapi.kakao.dto.KakaoBlogSearchResponse;
import com.test.search.externalapi.kakao.dto.KakaoBlogSearchResponse.Document;
import com.test.search.externalapi.kakao.dto.KakaoBlogSearchResponse.Meta;
import com.test.search.externalapi.kakao.validator.KakaoBlogSearchValidator;
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
class KakaoSearchSourceImplTest {
    @InjectMocks
    private KakaoSearchSourceImpl kakaoSearchSource;
    @Mock
    private RestTemplate kakaoRestTemplate;
    @Mock
    private KakaoBlogSearchValidator KakaoBlogSearchValidator;


    @Test
    @DisplayName("블로그 검색 결과 성공")
    void testSearchBlogs() {
        KakaoBlogSearchResponse kakaoBlogSearchResponse = KakaoBlogSearchResponse.builder()
                .meta(Meta.builder()
                    .pageable_count(1)
                    .total_count(1).build())
                .documents(Arrays.asList(
                    Document.builder().title("test1").build(),
                    Document.builder().title("test2").build()))
                .build();

        ResponseEntity<KakaoBlogSearchResponse> responseEntity = new ResponseEntity<>(
            kakaoBlogSearchResponse, HttpStatus.OK);

        when(kakaoRestTemplate.getForEntity(any(String.class), eq(KakaoBlogSearchResponse.class))).thenReturn(responseEntity);

        SearchResult searchResult = kakaoSearchSource.searchBlogs("test", "accuracy", 1, 10);

        assertThat(searchResult.getContent().size()).isEqualTo(2);
        assertThat(searchResult.getContent().get(0).getTitle()).isEqualTo("test1");
        assertThat(searchResult.getContent().get(1).getTitle()).isEqualTo("test2");
        assertThat(searchResult.getTotalPages()).isEqualTo(1);
        assertThat(searchResult.getTotalElements()).isEqualTo(1);
        verify(kakaoRestTemplate, times(1)).getForEntity(any(String.class), eq(
            KakaoBlogSearchResponse.class));
    }

    @Test
    @DisplayName("블로그 검색 결과, api connection error로 실패")
    void testSearchBlogsApiConnectionErrorException() {
        when(kakaoRestTemplate.getForEntity(any(String.class), eq(KakaoBlogSearchResponse.class)))
            .thenThrow(HttpClientErrorException.class);
        assertThatThrownBy(() -> kakaoSearchSource.searchBlogs("test", "accuracy", 1, 10))
            .isInstanceOf(ExternalApiException.class)
                .hasMessageContaining(ExternalApiErrorCode.API_CONNECTION_ERROR.getMessage());
    }

    @Test
    @DisplayName("블로그 검색 결과, api connection error로 실패")
    void testSearchBlogsUrlInvalidErrorException() {
        when(kakaoRestTemplate.getForEntity(any(String.class), eq(KakaoBlogSearchResponse.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        assertThatThrownBy(() -> kakaoSearchSource.searchBlogs("test", "accuracy", 1, 10))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.URL_INVALID.getMessage());
    }
}