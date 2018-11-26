package com.workbei.exception;

/**
 * @author Wallace Mao
 * Date: 2018-11-07 1:36
 */
public class WorkbeiRuntimeException extends RuntimeException {
    public WorkbeiRuntimeException() {
    }

    public WorkbeiRuntimeException(String message) {
        super(message);
    }

    public WorkbeiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkbeiRuntimeException(Throwable cause) {
        super(cause);
    }

    public WorkbeiRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
