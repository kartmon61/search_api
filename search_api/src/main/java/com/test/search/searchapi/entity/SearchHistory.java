package com.test.search.searchapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "search_history")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keyword_id", nullable = false)
    private SearchKeyword keyword;

    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt;

    @Column(name = "source_name", nullable = false)
    private String sourceName;

    public void setSearchKeyword(SearchKeyword keyword) {
        if (this.keyword != null) {
            this.keyword.getSearchHistoryList().remove(this);
        }
        this.keyword = keyword;
    }
}
