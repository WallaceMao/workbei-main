package com.workbei.controller.util;

import com.workbei.controller.autocreate.validator.CreateTeamValidator;
import com.workbei.exception.HttpResultException;
import com.workbei.http.HttpResultCode;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-12-07 1:20
 */
@Component("validateChecker")
public class ValidateChecker{
    public void check(Object object, Validator validator, Errors errors){
        if(validator == null){
            return;
        }
        if(errors.hasErrors()){
            findAndThrowOneError(errors);
        }
        validator.validate(object, errors);
        if(errors.hasErrors()){
            findAndThrowOneError(errors);
        }
    }

    private void findAndThrowOneError(Errors errors){
        List<ObjectError> errorList = errors.getAllErrors();
        ObjectError error = errorList.get(0);
        throw new HttpResultException(HttpResultCode.getByCode(error.getCode()));
    }
}
