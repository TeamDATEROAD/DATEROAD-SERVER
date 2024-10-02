package org.dateroad.mdc;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Slf4j
public class MDCFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpReq = WebUtils.getNativeRequest(request, HttpServletRequest.class);

        MDCManager.setJsonValue(MDCManager.MDC_REQUEST_URI, MDCManager.getRequestUri(httpReq));
        MDCManager.setJsonValue(MDCManager.MDC_USER_IP, MDCManager.getUserIP(httpReq));
        MDCManager.setJsonValue(MDCManager.MDC_REQUEST_COOKIES, MDCManager.getCookies(httpReq));
        MDCManager.setJsonValue(MDCManager.MDC_REQUEST_ORIGIN, MDCManager.getRequestOrigin(httpReq));
        MDCManager.setJsonValue(MDCManager.MDC_HEADER, MDCManager.getHeader(httpReq));
        MDCManager.setJsonValue(MDCManager.MDC_PARAMETER, MDCManager.getParameter(httpReq));
        MDCManager.set(MDCManager.MDC_BODY, MDCManager.getBody(httpReq));

        filterChain.doFilter(request, response);
    }
}
