package org.dateroad.common;

import org.dateroad.code.FailureCode;
import org.dateroad.code.SuccessCode;
import org.springframework.http.ResponseEntity;

public interface ApiResponseUtil {
    public static ResponseEntity<SuccessResponse<?>> success(SuccessCode successCode) {
        return ResponseEntity.status(successCode.getHttpStatus())
                .body(SuccessResponse.of(successCode));
    }

    public static <T> ResponseEntity<SuccessResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity.status(successCode.getHttpStatus())
                .body(SuccessResponse.of(successCode, data));
    }

    public static ResponseEntity<FailureResponse> failure(FailureCode failureCode) {
        return ResponseEntity.status(failureCode.getHttpStatus())
                .body(FailureResponse.of(failureCode));
    }
}
