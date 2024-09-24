package org.dateroad.exception;

import org.dateroad.code.FailureCode;

public class ConflictException extends DateRoadException {
    public ConflictException() {
        super(FailureCode.CONFLICT);
    }

    public ConflictException(FailureCode failureCode) {
        super(failureCode);
    }
}
