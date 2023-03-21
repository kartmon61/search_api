package com.test.search.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private ErrorCode errorCode;

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
