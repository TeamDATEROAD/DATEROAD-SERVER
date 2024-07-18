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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailureResponse {

    private String message;
    private HttpStatus status;
    private List<FieldError> errors;
    private String code;


    private FailureResponse(final FailureCode code, final List<FieldError> errors) {
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
        final List<FailureResponse.FieldError> errors = FailureResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return new FailureResponse(FailureCode.INVALID_TYPE_VALUE, errors);
    }

    public static FailureResponse of(HttpMessageNotReadableException e) {
        String message = e.getMessage();
        int typeIndex = message.indexOf("Cannot deserialize value of type `");
        int fromIndex = message.indexOf("from String \"");
        int notOneOfIndex = message.indexOf("not one of the values accepted for Enum class: [");

        if (typeIndex != -1 && fromIndex != -1 && notOneOfIndex != -1) {
            String fieldType = message.substring(typeIndex + 34, fromIndex);
            String fieldValue = message.substring(fromIndex + 13, notOneOfIndex - 1);
            String acceptedValues = message.substring(notOneOfIndex + 45, message.length() - 1);

            List<FailureResponse.FieldError> errors = new ArrayList<>();
            errors.add(new FailureResponse.FieldError(fieldType, fieldValue, acceptedValues));
            return new FailureResponse(FailureCode.INVALID_TYPE_VALUE, errors);
        } else {
            final String value = e.getMessage() == null ? "" : e.getHttpInputMessage().toString();
            final List<FailureResponse.FieldError> errors = FailureResponse.FieldError.of(e.getMessage(), value, e.getHttpInputMessage().toString());
            return new FailureResponse(FailureCode.INVALID_TYPE_VALUE, errors);
        }
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

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

}