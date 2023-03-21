package com.test.search.externalapi.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SearchType {

    KAKAO("kakao"),
    NAVER("naver");
    private String searchType;

    public static SearchType fromString(String columnType) {
        for (SearchType enumItem : SearchType.values()) {
            if (enumItem.searchType.equalsIgnoreCase(columnType)) {
                return enumItem;
            }
        }
        return null;
    }
}
