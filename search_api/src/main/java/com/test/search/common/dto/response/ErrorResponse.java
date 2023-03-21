package com.test.search.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private boolean success;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
        this.success = false;
    }
}
