package org.dateroad.feign.discord;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.mdc.MDCManager;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Profile("!local")
@Component
@Setter
public class DiscordLogAppender extends AppenderBase<ILoggingEvent> {
    private String discordLogUrl;

    @Override
    public void start() {
        if (discordLogUrl == null || discordLogUrl.isEmpty() ) {
            addError("Discord is not configured properly. Please set the discordLogUrl");
            return;
        }
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Map<String, String> mdcPropertyMap = eventObject.getMDCPropertyMap();

        String level = eventObject.getLevel().levelStr;
        String exceptionBrief = "";
        String exceptionDetail = "";
        IThrowableProxy throwable = eventObject.getThrowableProxy();

        if (throwable != null) {
            exceptionBrief = throwable.getClassName() + ": " + throwable.getMessage();
        }

        if (exceptionBrief.equals("")) {
            exceptionBrief = "EXCEPTION 정보가 남지 않았습니다.";
        }

        StringBuilder logMessage = new StringBuilder();

        appendWithNewLine(logMessage, "[" + level + " - 문제 간략 내용] " + exceptionBrief);
        appendWithNewLine(logMessage,  "[Time] " +  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        appendWithNewLine(logMessage, "[" + MDCManager.MDC_REQUEST_URI + "] " + StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCManager.MDC_REQUEST_URI)));
        appendWithNewLine(logMessage, "[" + MDCManager.MDC_USER_IP + "] " + StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCManager.MDC_USER_IP)));
        appendWithNewLine(logMessage, "[" + MDCManager.MDC_HEADER + "] " + StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCManager.MDC_HEADER).replaceAll("[\\{\\{\\}]", "")));
        appendWithNewLine(logMessage, "[" + MDCManager.MDC_REQUEST_COOKIES + "] " + StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCManager.MDC_REQUEST_COOKIES).replaceAll("[\\{\\{\\}]", "")));
        appendWithNewLine(logMessage, "[" + MDCManager.MDC_PARAMETER + "] " + StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCManager.MDC_PARAMETER).replaceAll("[\\{\\{\\}]", "")));
        appendWithNewLine(logMessage, "[" + MDCManager.MDC_BODY + "] " + StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCManager.MDC_BODY)));

        sendToDiscord(logMessage.toString());

        if (throwable != null) {
            exceptionDetail = ThrowableProxyUtil.asString(throwable);
            String exception = "[Exception 상세 내용] " + exceptionDetail.substring(0, 1000);
            sendToDiscord(exception);
        }
    }

    private void appendWithNewLine(StringBuilder sb, String text) {
        sb.append(text).append("\n");
    }

    private void sendToDiscord(String logMessage) {
        if (logMessage.length() > 2000) {
            logMessage = logMessage.substring(0, 1950) + "......";
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = new HashMap<>();
        payload.put("content", logMessage);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(discordLogUrl, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                addError("Failed to send log message to Discord: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Exception occurred while sending log message to Discord", e);
        }
    }
}
