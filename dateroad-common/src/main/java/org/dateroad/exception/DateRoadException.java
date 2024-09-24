package org.dateroad.exception;

import lombok.Getter;
import org.dateroad.code.FailureCode;

@Getter
public class DateRoadException extends RuntimeException {
    private final FailureCode failureCode;

    public DateRoadException(FailureCode failureCode) {
        super(failureCode.getMessage());
        this.failureCode = failureCode;
    }

    public DateRoadException(String message, FailureCode errorCode) {
        super(message);
        this.failureCode = errorCode;
    }
}
