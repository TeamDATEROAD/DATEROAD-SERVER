package org.dateroad.exception;

import org.dateroad.code.FailureCode;

public class BadRequestException extends DateRoadException {
    public BadRequestException() {
        super(FailureCode.BAD_REQUEST);
    }

    public BadRequestException(FailureCode failureCode) {
        super(failureCode);
    }
}
