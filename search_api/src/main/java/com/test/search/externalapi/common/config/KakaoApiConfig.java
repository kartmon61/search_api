package com.test.search.externalapi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KakaoApiConfig {

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    /**
     * kakao api 호출시 헤더에 authorization api key 추가
     * @return RestTemplate
     */
    @Bean
    public RestTemplate kakaoRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
            return execution.execute(request, body);
        };

        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }
}
