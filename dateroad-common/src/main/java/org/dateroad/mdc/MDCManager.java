package org.dateroad.mdc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MDCManager {
    public static final String MDC_REQUEST_URI = "Request URI";
    public static final String MDC_USER_IP = "사용자 IP";
    public static final String MDC_REQUEST_COOKIES = "Request Cookie";
    public static final String MDC_REQUEST_ORIGIN = "Request Origin";
    public static final String MDC_HEADER = "HTTP Header";
    public static final String MDC_PARAMETER = "Parameter";
    public static final String MDC_BODY = "HTTP Body";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MDCAdapter mdcAdapter = MDC.getMDCAdapter();
    public static void set(String key, String value) {
        mdcAdapter.put(key, value);
    }

    public static Object get(String key) {
        return mdcAdapter.get(key);
    }

    public static void setJsonValue(String key, Object value) throws JsonProcessingException {
        try {
            if (value != null) {
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
                mdcAdapter.put(key, json);
            } else {
                mdcAdapter.put(key, "내용이 없습니다.");
            }
        } catch (JsonProcessingException ex) {
            throw ex;
        }
    }

    public static String getUserIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null)
            ip = request.getRemoteAddr();

        return ip;
    }

    public static Cookie[] getCookies(HttpServletRequest request) {
        return request.getCookies();
    }

    public static String getRequestOrigin(HttpServletRequest request) {
        return request.getHeader("Origin");
    }

    public static Map<String, String> getHeader(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    if (!name.equals("user-agent")) {
                        headerMap.put(name, request.getHeader(name));
                    }
                });
        return headerMap;
    }

    public static Map<String, String> getParameter(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(name -> paramMap.put(name, request.getParameter(name)));

        return paramMap;
    }

    public static String getBody(HttpServletRequest request) {
        RequestBodyWrapper requestBodyWrapper = WebUtils.getNativeRequest(request, RequestBodyWrapper.class);

        if (requestBodyWrapper != null) {
            return requestBodyWrapper.getRequestBody();
        }

        return "requestBody 정보 없음";
    }
}
