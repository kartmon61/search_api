package com.test.search.searchapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PopularKeywordResponse {
    private String keyword;
    private Long count;
}
