package org.dateroad.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.dateroad.code.SuccessCode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record SuccessResponse<T>(
        int status,
        String message,
        @JsonInclude(value = NON_NULL)
        T data
) {
    public static <T> SuccessResponse<T> of(SuccessCode successCode, T data) {
        return SuccessResponse.<T>builder()
                .status(successCode.getHttpStatus().value())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    public static SuccessResponse<?> of(SuccessCode successCode) {
        return SuccessResponse.builder()
                .status(successCode.getHttpStatus().value())
                .message(successCode.getMessage())
                .build();
    }
}
