package com.test.search.searchapi.service;


import com.test.search.common.dto.response.SearchResult;
import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import com.test.search.externalapi.common.type.SearchType;
import com.test.search.externalapi.common.source.SearchSource;
import com.test.search.externalapi.common.source.SearchSourceFactory;
import com.test.search.searchapi.dto.PopularKeywordResponse;
import com.test.search.searchapi.entity.SearchHistory;
import com.test.search.searchapi.entity.SearchKeyword;
import com.test.search.searchapi.repository.SearchHistoryRepository;
import com.test.search.searchapi.repository.SearchKeywordRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogSearchService {

    private final SearchKeywordRepository searchKeywordRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final SearchSourceFactory searchSourceFactory;

    /**
     * 블로그 검색 및 해당 키워드 저장 로직 수행
     * @TODO:
     *      - 동시성 이슈 처리를 위해 캐시, 배치 전략 적용 필요
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return SerchResult
     */
    @Transactional
    public SearchResult searchBlogs(String query, String sort, Integer page, Integer size) {
        SearchSource searchSource = searchSourceFactory.createSearchSource(SearchType.KAKAO);
        try {
            SearchResult searchResult = searchSource.searchBlogs(query, sort, page, size);
            saveSearchKeywordAndHistory(query, SearchHistory.builder()
                .searchedAt(LocalDateTime.now())
                .sourceName(searchSource.getSourceName())
                .build());
            return searchResult;
        } catch (ExternalApiException ex) {
            // 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터를 제공함
            if (ex.getMessage().equals(ExternalApiErrorCode.API_CONNECTION_ERROR.getMessage())) {
                searchSource = searchSourceFactory.createSearchSource(SearchType.NAVER);
                SearchResult searchResult = searchSource.searchBlogs(query, sort, page, size);
                saveSearchKeywordAndHistory(query, SearchHistory.builder()
                    .searchedAt(LocalDateTime.now())
                    .sourceName(searchSource.getSourceName())
                    .build());
                return searchResult;
            }
            throw ex;
        }
    }

    /**
     * 검색 키워드와 히스토리 저장하는 함수
     * @param query
     * @param searchHistory
     */
    private void saveSearchKeywordAndHistory(String query, SearchHistory searchHistory) {
        SearchKeyword searchKeyword = searchKeywordRepository.findByKeyword(query)
            .orElse(new SearchKeyword(query));
        searchKeyword.incrementCount();
        searchKeyword.addSearchHistory(searchHistory);
        searchKeywordRepository.save(searchKeyword);
        searchHistoryRepository.save(searchHistory);
    }



    /**
     * 상위 10개 인기검색어 정보 가져오는 로직 수행
     * @return List<PopularKeywordResponse>
     */
    public List<PopularKeywordResponse> getPopularKeywords() {
        List<SearchKeyword> result = searchKeywordRepository.findTop10ByOrderByCountDesc();
        return result.stream().map(data -> PopularKeywordResponse.builder()
                .keyword(data.getKeyword())
                .count(data.getCount())
                .build())
            .collect(Collectors.toList());
    }
}
