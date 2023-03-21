package com.test.search.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;

    public BaseResponse(boolean success){
        this.success = success;
        this.data = null;
        this.message = null;
    }

    public BaseResponse(boolean success, T data){
        this.success = success;
        this.data = data;
        this.message = null;
    }

    public BaseResponse(boolean success, T data, String message){
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static BaseResponse<?> ok(){
        return new BaseResponse<>(true);
    }

    public static <T> BaseResponse<T> ok(T data){
        return new BaseResponse<T>(true, data);
    }

    public static BaseResponse<?> error(String message){
        return new BaseResponse<>(false, null, message);
    }
}
