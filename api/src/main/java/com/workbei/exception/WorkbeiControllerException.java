package com.workbei.exception;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 18:32
 */
public class WorkbeiControllerException extends RuntimeException{
    public WorkbeiControllerException() {
    }

    public WorkbeiControllerException(String message) {
        super(message);
    }

    public WorkbeiControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkbeiControllerException(Throwable cause) {
        super(cause);
    }

    public WorkbeiControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
