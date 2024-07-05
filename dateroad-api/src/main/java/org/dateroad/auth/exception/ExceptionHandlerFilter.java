package org.dateroad.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.common.Constants;
import org.dateroad.common.FailureResponse;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            handleUnauthorizedException(response, e);
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        UnauthorizedException unAuthorizedException = (UnauthorizedException) e;
        FailureCode errorMessage = unAuthorizedException.getFailureCode();
        HttpStatus httpStatus = errorMessage.getHttpStatus();
        setResponse(response, httpStatus, errorMessage);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        log.error(">>> Exception Handler Filter : ", e);
        setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, FailureCode.INTERNAL_SERVER_ERROR);
    }

    private void setResponse(HttpServletResponse response, HttpStatus httpStatus, FailureCode errorMessage) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Constants.CHARACTER_TYPE);
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(FailureResponse.of(errorMessage)));
    }
}
