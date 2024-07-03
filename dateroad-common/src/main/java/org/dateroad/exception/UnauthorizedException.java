package org.dateroad.exception;

import org.dateroad.code.FailureCode;

public class UnauthorizedException extends DateRoadException {
    public UnauthorizedException() {
        super(FailureCode.UNAUTHORIZED);
    }

    public UnauthorizedException(FailureCode failureCode) {
        super(failureCode);
    }
}
