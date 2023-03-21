package com.test.search.searchapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "search_keyword")
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    @Column(nullable = false)
    @Setter
    private Long count;

    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY)
    private List<SearchHistory> searchHistoryList = new ArrayList<>();

    public SearchKeyword(String keyword) {
        this.keyword = keyword;
        this.count = 1L;
        this.searchHistoryList = new ArrayList<>();
    }

    /**
     * 검색 히스토리 추가
     * @param searchHistory
     */
    public void addSearchHistory(SearchHistory searchHistory) {
        searchHistoryList.add(searchHistory);
        searchHistory.setSearchKeyword(this);
    }

    /**
     * 검색 키워드 카운트 증가
     */
    public void incrementCount() {
        count++;
    }
}
