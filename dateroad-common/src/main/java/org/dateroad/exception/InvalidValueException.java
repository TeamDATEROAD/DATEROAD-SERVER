package org.dateroad.exception;

import org.dateroad.code.FailureCode;

public class InvalidValueException extends DateRoadException {
    public InvalidValueException() {
        super(FailureCode.BAD_REQUEST);
    }

    public InvalidValueException(FailureCode failureCode) {
        super(failureCode);
    }
}
