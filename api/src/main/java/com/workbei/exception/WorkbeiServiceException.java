package com.workbei.exception;

/**
 * @author Wallace Mao
 * Date: 2018-12-03 17:22
 */
public class WorkbeiServiceException extends RuntimeException {
    public WorkbeiServiceException() {
    }

    public WorkbeiServiceException(String message) {
        super(message);
    }

    public WorkbeiServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkbeiServiceException(Throwable cause) {
        super(cause);
    }

    public WorkbeiServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
