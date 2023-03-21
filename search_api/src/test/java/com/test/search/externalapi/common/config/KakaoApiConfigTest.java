package com.test.search.externalapi.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

class KakaoApiConfigTest {
    private KakaoApiConfig kakaoApiConfig;
    private final String AUTHORIZATION = "Authorization";

    @BeforeEach
    void setUp() {
        kakaoApiConfig = new KakaoApiConfig();
    }

    @Test
    @DisplayName("KakaoApiConfig.getRestTemplate() 실행시 헤더에 kakao_api_key를 추가한다.")
    void testKakaoRestTemplate() throws IOException {
        RestTemplate restTemplate = kakaoApiConfig.kakaoRestTemplate();
        ClientHttpRequestInterceptor interceptor = restTemplate.getInterceptors().get(0);

        ClientHttpRequest request = mock(ClientHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);

        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(request.execute()).thenReturn(response);

        interceptor.intercept(request, null, (req, body) -> response);

        assertThat(headers.get(AUTHORIZATION)).hasSize(1);
        assertThat(headers.get(AUTHORIZATION).get(0)).isNotEmpty();
    }
}