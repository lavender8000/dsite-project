package com.lav.dsite.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;


public class LogManager {

    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);

    private static final String LOG_PREFIX = "[DSITE] ";

    public static Logger getLogger(Class<?> clazz) {
        return logger;
    }

    public static void info(String module, String message) {
        logger.info(formatMessage(module, message));
    }

    public static void debug(String module, String message) {
        logger.debug(formatMessage(module, message));
    }

    public static void warn(String module, String message) {
        logger.warn(formatMessage(module, message));
    }

    public static void error(String module, String message) {
        logger.error(formatMessage(module, message));
    }

    public static void error(String module, String message, Throwable throwable) {
        logger.error(formatMessage(module, message), throwable);
    }

    public static void exception(Exception e) {
        error(e.getClass().getName(), e.getMessage());
    }

    public static void exception(Exception e, Throwable throwable) {
        error(e.getClass().getName(), e.getMessage(), throwable);
    }

    public static void exceptionWithUrl(Exception e, HttpServletRequest request) {
        logger.error(formatMessageWithUrl(e.getClass().getName(), e.getMessage(), request.getRequestURL().toString(), request.getMethod()));
    }

    private static String formatMessage(String module, String message) {
        return String.format("%s%s: %s", LOG_PREFIX, module, message);
    }
    
    private static String formatMessageWithUrl(String module, String message, String requestUrl, String method) {
        return String.format("%s%s: %s \n    (Request URL: %s, Method: '%s')", LOG_PREFIX, module, message, requestUrl, method);
    }
}
