package org.dateroad.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.code.FailureCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailureResponse {

    private String message;
    private HttpStatus status;
    private List<FieldError> errors;
    private String code;


    protected FailureResponse(final FailureCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getHttpStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    private FailureResponse(final FailureCode code) {
        this.message = code.getMessage();
        this.status = code.getHttpStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }

    public static FailureResponse of(final FailureCode code, final BindingResult bindingResult) {
        return new FailureResponse(code, FieldError.of(bindingResult));
    }

    public static FailureResponse of(final FailureCode code) {
        return new FailureResponse(code);
    }

    public static FailureResponse of(final FailureCode code, final List<FieldError> errors) {
        return new FailureResponse(code, errors);
    }

    public static FailureResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FailureResponse.FieldError> errors = FailureResponse.FieldError.of(e.getName(), value,
                e.getErrorCode());
        return new FailureResponse(FailureCode.INVALID_TYPE_VALUE, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        protected static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .toList();
        }
    }
}