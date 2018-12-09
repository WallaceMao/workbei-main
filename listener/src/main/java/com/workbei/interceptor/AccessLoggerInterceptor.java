package com.workbei.interceptor;

import com.workbei.util.LogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

import static com.workbei.util.LogFormatter.getKV;
import static com.workbei.util.LogFormatter.LogEvent;

/**
 * @author Wallace Mao
 * Date: 2018-12-08 16:05
 */
public class AccessLoggerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger accessLogger = LoggerFactory.getLogger(AccessLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        accessLogger.debug(LogFormatter.format(
                LogEvent.START,
                "http request",
                getKV("method: ", request.getMethod()),
                getKV("url: ", request.getRequestURI()),
                getKV("query: ", request.getQueryString()),
                getKV("body: ", request.getReader().lines().collect(
                        Collectors.joining(System.lineSeparator())
                ))
        ));
        return true;
    }
}
