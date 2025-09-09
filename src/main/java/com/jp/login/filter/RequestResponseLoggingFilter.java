package com.jp.login.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger requestLogger = LoggerFactory.getLogger("com.jp.login.logging.request");
    private static final Logger responseLogger = LoggerFactory.getLogger("com.jp.login.logging.response");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        // wrap to cache body
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse) response);

        HttpServletRequest req = wrappedRequest;
        HttpServletResponse res = wrappedResponse;

        Instant start = Instant.now();

        // Log basic request info
        String reqMsg = String.format("%s %s from=%s", req.getMethod(), req.getRequestURI(), req.getRemoteAddr());
        requestLogger.info(reqMsg);

        chain.doFilter(wrappedRequest, wrappedResponse);

        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();

        String resMsg = String.format("%s %s -> status=%d time=%dms", req.getMethod(), req.getRequestURI(),
                res.getStatus(), millis);
        responseLogger.info(resMsg);

        // log bodies (with reasonable size cap)
        logRequestBody(wrappedRequest);
        logResponseBody(wrappedResponse);

        // copy response body back to actual response
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf == null || buf.length == 0) {
            return;
        }
        int length = Math.min(buf.length, 8192); // cap at 8KB
        String payload = new String(Arrays.copyOf(buf, length), StandardCharsets.UTF_8);
        requestLogger.debug("Request body ({} bytes): {}", buf.length, payload);
    }

    private void logResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf == null || buf.length == 0) {
            return;
        }
        int length = Math.min(buf.length, 8192);
        String payload = new String(Arrays.copyOf(buf, length), StandardCharsets.UTF_8);
        responseLogger.debug("Response body ({} bytes): {}", buf.length, payload);
    }
}
