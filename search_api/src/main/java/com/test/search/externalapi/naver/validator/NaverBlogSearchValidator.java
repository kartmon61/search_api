package com.test.search.externalapi.naver.validator;

import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import com.test.search.externalapi.common.validator.BlogSearchValidator;
import org.springframework.stereotype.Component;

@Component
public class NaverBlogSearchValidator implements BlogSearchValidator {

    /**
     * 정렬 관련 검증 로직
     * @param sort
     * @return String
     */
    @Override
    public String validateSort(String sort) {
        if (sort != null && sort.equals("accuracy")) {
            return "sim";
        } else if (sort != null && sort.equals("recency")) {
            return "date";
        }

        if (sort != null && !sort.equalsIgnoreCase("sim")
            && !sort.equalsIgnoreCase("date")) {
            throw new ExternalApiException(ExternalApiErrorCode.SORT_VALUE_INVALID);
        }

        return sort;
    }

    /**
     * 페이지 관련 검증 로직
     * @param page
     * @return Integer
     */
    @Override
    public Integer validatePage(Integer page) {
        if (page != null && page < 1) {
            throw new ExternalApiException(ExternalApiErrorCode.PAGE_NUMBER_INVALID);
        }

        return page;
    }

    /**
     * 사이즈 관련 검증 로직
     * @param size
     * @return Integer
     */
    @Override
    public Integer validateSize(Integer size) {
        if (size != null && (size < 1 || size > 50)) {
            throw new ExternalApiException(ExternalApiErrorCode.PAGE_SIZE_INVALID);
        }

        return size;
    }
}
