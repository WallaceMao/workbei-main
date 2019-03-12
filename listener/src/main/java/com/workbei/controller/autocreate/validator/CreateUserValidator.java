package com.workbei.controller.autocreate.validator;

import com.workbei.http.HttpResultCode;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 20:52
 */
@Component("createUserValidator")
public class CreateUserValidator implements Validator{
    @Override
    public boolean supports(Class<?> aClass) {
        return AutoCreateUserVO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AutoCreateUserVO user = (AutoCreateUserVO)o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "outerCorpId",
                HttpResultCode.USER_OUTER_CORP_ID_NULL.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "outerCombineId",
                HttpResultCode.USER_OUTER_ID_NULL.getCode());
        if (user.getOuterCombineDeptIdList() == null) {
            errors.rejectValue("outerCombineDeptIdList", HttpResultCode.USER_OUTER_DEPT_ID_NULL.getCode());
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                HttpResultCode.USER_NAME_NULL.getCode());
    }
}
