package org.dateroad.common;

import lombok.Builder;
import org.dateroad.code.FailureCode;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record FailureResponse(
        int status,
        String code,
        String message
) {
    public static FailureResponse of(FailureCode failureCode) {
        return FailureResponse.builder()
                .status(failureCode.getHttpStatus().value())
                .code(failureCode.getCode())
                .message(failureCode.getMessage())
                .build();
    }
}
