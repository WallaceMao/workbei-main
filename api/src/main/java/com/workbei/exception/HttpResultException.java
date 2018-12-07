package com.workbei.exception;

import com.workbei.http.HttpResultCode;

/**
 * @author Wallace Mao
 * Date: 2018-12-03 17:22
 */
public class HttpResultException extends RuntimeException {
    private HttpResultCode httpResultCode;

    public HttpResultException() {
    }

    public HttpResultException(String message) {
        super(message);
    }

    public HttpResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResultException(Throwable cause) {
        super(cause);
    }

    public HttpResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpResultException(HttpResultCode httpResultCode) {
        this.httpResultCode = httpResultCode;
    }

    public HttpResultCode getHttpResultCode() {
        return httpResultCode;
    }

    public void setHttpResultCode(HttpResultCode httpResultCode) {
        this.httpResultCode = httpResultCode;
    }
}
