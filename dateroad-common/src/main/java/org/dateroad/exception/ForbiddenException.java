package org.dateroad.exception;

import org.dateroad.code.FailureCode;

public class ForbiddenException extends DateRoadException {
    public ForbiddenException() {
        super(FailureCode.FORBIDDEN);
    }

    public ForbiddenException(FailureCode failureCode) {
        super(failureCode);
    }
}
