package com.workbei.exception;

import com.workbei.http.HttpResultCode;

/**
 * @author Wallace Mao
 * Date: 2018-12-03 17:22
 */
public class HttpAuthException extends RuntimeException {
    private HttpResultCode httpResultCode;

    public HttpAuthException() {
    }

    public HttpAuthException(String message) {
        super(message);
    }

    public HttpAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpAuthException(Throwable cause) {
        super(cause);
    }

    public HttpAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpAuthException(HttpResultCode httpResultCode) {
        this.httpResultCode = httpResultCode;
    }

    public HttpResultCode getHttpResultCode() {
        return httpResultCode;
    }

    public void setHttpResultCode(HttpResultCode httpResultCode) {
        this.httpResultCode = httpResultCode;
    }
}
