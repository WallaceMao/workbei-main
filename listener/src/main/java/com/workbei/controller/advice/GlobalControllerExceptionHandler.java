package com.workbei.controller.advice;

import com.workbei.controller.util.ResponseResult;
import com.workbei.exception.ExceptionCode;
import com.workbei.exception.HttpResultException;
import com.workbei.exception.WorkbeiServiceException;
import com.workbei.http.HttpResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.workbei.exception.ExceptionCode.APP_REQUEST_PARAM_ERROR;
import static com.workbei.exception.ExceptionCode.APP_SYSTEM_ERROR;

/**
 * 用来捕获com.workbei.controller下抛出的异常
 * @author Wallace Mao
 * Date: 2018-12-06 18:35
 */
@ControllerAdvice(basePackages = "com.workbei.controller")
public class GlobalControllerExceptionHandler {
    private static final Logger bizLogger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(HttpResultException.class)
    public ResponseEntity handleHttpResultException(HttpResultException exception){
        try{
            bizLogger.error(ExceptionCode.getMessage(APP_REQUEST_PARAM_ERROR), exception);
            HttpResultCode code = exception.getHttpResultCode();
            if(code == null){
                code = HttpResultCode.failCode();
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.fail(code));
        } catch (Exception e){
            bizLogger.error("handleWorkbeiServiceException error", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error");
        }
    }
    /**
     * 通用Exception，捕获所有的Exception
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleDefaultControllerException(Exception exception){
        try{
            bizLogger.error(ExceptionCode.getMessage(APP_SYSTEM_ERROR), exception);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.fail());
        } catch (Exception e){
            bizLogger.error("handleDefaultControllerException error", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error");
        }
    }
}
