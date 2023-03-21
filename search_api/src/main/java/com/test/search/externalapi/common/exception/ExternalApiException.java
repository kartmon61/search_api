package com.test.search.externalapi.common.exception;

import com.test.search.common.exception.BaseException;

public class ExternalApiException extends BaseException {
    public ExternalApiException(ExternalApiErrorCode errorCode) {
        super(
            errorCode.getErrorCode(),
            errorCode.getMessage() == null ? errorCode.getErrorCode().getMessage() : errorCode.getMessage()
        );
    }
}
