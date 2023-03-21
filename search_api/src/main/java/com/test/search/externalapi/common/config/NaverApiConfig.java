package com.test.search.externalapi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NaverApiConfig {

    @Value("${naver.api.client-id}")
    private String NAVER_API_CLIENT_ID;

    @Value("${naver.api.client-secret}")
    private String NAVER_API_CLIENT_SECRET;

    /**
     * naver api 호출시 헤더에 api key 추가
     * @return
     */
    @Bean
    public RestTemplate naverRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.set("X-Naver-Client-Id", NAVER_API_CLIENT_ID);
            headers.set("X-Naver-Client-Secret", NAVER_API_CLIENT_SECRET);
            return execution.execute(request, body);
        };

        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }
}
