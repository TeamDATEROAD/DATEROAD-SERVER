package org.dateroad.exception;

import org.dateroad.code.FailureCode;

public class EntityNotFoundException extends DateRoadException {
    public EntityNotFoundException() {
        super(FailureCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(FailureCode failureCode) {
        super(failureCode);
    }
}
