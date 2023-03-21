package com.test.search.externalapi.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.search.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExternalApiErrorCode {

    URL_INVALID(ErrorCode.BAD_REQUEST_EXCEPTION, "올바르지 못한 API Url 입니다."),
    SORT_VALUE_INVALID(ErrorCode.BAD_REQUEST_EXCEPTION, "정렬 옵션은 'accuracy' 또는 'recency' 중 하나여야 합니다."),
    PAGE_NUMBER_INVALID(ErrorCode.BAD_REQUEST_EXCEPTION, "페이지 번호는 1 이상이어야 합니다."),
    PAGE_SIZE_INVALID(ErrorCode.BAD_REQUEST_EXCEPTION, "페이지 크기는 1 이상 50 이하이어야 합니다."),
    API_CONNECTION_ERROR(ErrorCode.INTERNAL_SERVER_ERROR, "블로그 검색 API와의 통신에 문제가 발생했습니다.");


    private ErrorCode errorCode;
    private final String message;
}
