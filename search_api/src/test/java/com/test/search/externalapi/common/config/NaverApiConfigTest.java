package com.test.search.externalapi.common.config;

import static org.assertj.core.api.Assertions.*;
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

class NaverApiConfigTest {
    private NaverApiConfig naverApiConfig;
    private final String NAVER_CLIENT_ID = "X-Naver-Client-Id";
    private final String NAVER_CLIENT_SECRET = "X-Naver-Client-Secret";

    @BeforeEach
    void setUp() {
        naverApiConfig = new NaverApiConfig();
    }

    @Test
    @DisplayName("NaverApiConfig.getRestTemplate() 실행시 헤더에 naver_api_key를 추가한다.")
    void testKakaoRestTemplate() throws IOException {
        RestTemplate restTemplate = naverApiConfig.naverRestTemplate();
        ClientHttpRequestInterceptor interceptor = restTemplate.getInterceptors().get(0);

        ClientHttpRequest request = mock(ClientHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);

        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(request.execute()).thenReturn(response);

        interceptor.intercept(request, null, (req, body) -> response);

        assertThat(headers.get(NAVER_CLIENT_ID)).hasSize(1);
        assertThat(headers.get(NAVER_CLIENT_SECRET)).hasSize(1);
    }
}