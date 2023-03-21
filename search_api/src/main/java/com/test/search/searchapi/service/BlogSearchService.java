package com.test.search.searchapi.service;


import com.test.search.common.dto.response.SearchResult;
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
     *      - 카카오 에러 발생시 네이버 API 호출을 위한 처리 필요
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return SerchResult
     */
    @Transactional
    public SearchResult searchBlogs(String query, String sort, Integer page, Integer size) {
        SearchSource searchSource = searchSourceFactory.createSearchSource(SearchType.KAKAO);

        SearchResult searchResult = searchSource.searchBlogs(query, sort, page,
            size);
        SearchHistory searchHistory = SearchHistory.builder()
            .searchedAt(LocalDateTime.now())
            .sourceName(searchSource.getSourceName())
            .build();

        searchKeywordRepository.findByKeyword(query)
            .ifPresentOrElse(searchKeyword -> {
                    searchKeyword.incrementCount();
                    searchKeyword.addSearchHistory(searchHistory);
                    searchKeywordRepository.save(searchKeyword);
                    searchHistoryRepository.save(searchHistory);
                },
                () -> {
                    SearchKeyword searchKeyword = new SearchKeyword(query);
                    searchKeyword.addSearchHistory(searchHistory);
                    searchKeywordRepository.save(searchKeyword);
                    searchHistoryRepository.save(searchHistory);
                }
            );

        return searchResult;
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
