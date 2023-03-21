package com.test.search.externalapi.common.source;

import com.test.search.externalapi.common.type.SearchType;
import com.test.search.externalapi.kakao.source.KakaoSearchSourceImpl;
import com.test.search.externalapi.naver.source.NaverSearchSourceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SearchSourceFactory {
    private final KakaoSearchSourceImpl kakaoSearchSource;
    private final NaverSearchSourceImpl naverSearchSource;

    /**
     * search source를 생성, 올바른 타입이 없는 경우 kakao search source를 생성
     * @param searchType
     * @return
     */
    public SearchSource createSearchSource(SearchType searchType) {
        switch (searchType) {
            case KAKAO:
                return kakaoSearchSource;
            case NAVER:
                return naverSearchSource;
        }
        return kakaoSearchSource;
    }
}
