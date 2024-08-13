package org.dateroad.common;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.common.FailureResponse.FieldError;
import org.dateroad.exception.DateRoadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        final BindingResult bindingResult = e.getBindingResult();
        final List<FailureResponse.FieldError> errors = FailureResponse.FieldError.of(bindingResult);
        FailureResponse response = new FailureResponse(FailureCode.INVALID_TYPE_VALUE, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<FailureResponse> handleBindException(final BindException e) {
        log.error(">>> handle: BindException ", e);
        final FailureResponse response = FailureResponse.of(FailureCode.INVALID_INPUT_VALUE,e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<FailureResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = FailureResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ResponseEntity<>(new FailureResponse(FailureCode.INVALID_TYPE_VALUE, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<FailureResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        final FailureResponse response = FailureResponse.of(FailureCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DateRoadException.class)
    public ResponseEntity<FailureResponse> handleDateRoadException(final DateRoadException e) {
        log.error(">>> handle: DateRoadException ", e);
        final FailureCode errorCode = e.getFailureCode();
        final FailureResponse response = FailureResponse.of(errorCode);
        return new ResponseEntity<>(response,errorCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<FailureResponse> handleException(final Exception e) {
        log.error(">>> handle: Exception ", e);
        String errorMessage = e.getMessage() != null ? e.getMessage() : "Internal Server Error";
        List<FailureResponse.FieldError> errors = FailureResponse.FieldError.of("Exception", "", errorMessage);
        final FailureResponse response = FailureResponse.of(FailureCode.INTERNAL_SERVER_ERROR, errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
