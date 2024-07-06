package org.dateroad.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.dateroad.code.FailureCode;
import org.springframework.http.ResponseEntity;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FailureResponse {
    private final int status;
    private final String code;
    private final String message;

    public static FailureResponse of(FailureCode failureCode) {
        return builder()
                .status(failureCode.getHttpStatus().value())
                .code(failureCode.getCode())
                .message(failureCode.getMessage())
                .build();
    }

    public static ResponseEntity<FailureResponse> failure(FailureCode failureCode) {
        return ResponseEntity.status(failureCode.getHttpStatus())
                .body(FailureResponse.of(failureCode));
    }
}
