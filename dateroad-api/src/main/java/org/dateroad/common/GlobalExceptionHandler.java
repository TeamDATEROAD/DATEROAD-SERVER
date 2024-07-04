package org.dateroad.common;

import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.DateRoadException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<FailureResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);
        return FailureResponse.failure(FailureCode.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<FailureResponse> handleBindException(final BindException e) {
        log.error(">>> handle: BindException ", e);
        return  FailureResponse.failure(FailureCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<FailureResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        return FailureResponse.failure(FailureCode.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<FailureResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        return FailureResponse.failure(FailureCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DateRoadException.class)
    protected ResponseEntity<FailureResponse> handleBusinessException(final DateRoadException e) {
        log.error(">>> handle: DateRoadException ", e);
        return FailureResponse.failure(e.getFailureCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<FailureResponse> handleException(final Exception e) {
        log.error(">>> handle: Exception ", e);
        return FailureResponse.failure(FailureCode.INTERNAL_SERVER_ERROR);
    }
}
