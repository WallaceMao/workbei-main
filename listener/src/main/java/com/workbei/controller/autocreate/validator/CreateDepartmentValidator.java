package com.workbei.controller.autocreate.validator;

import com.workbei.http.HttpResultCode;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 20:52
 */
@Component("createDepartmentValidator")
public class CreateDepartmentValidator implements Validator{
    @Override
    public boolean supports(Class<?> aClass) {
        return AutoCreateDepartmentVO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "outerCorpId",
                HttpResultCode.DEPT_OUTER_CORP_ID_NULL.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "outerCombineId",
                HttpResultCode.DEPT_OUTER_ID_NULL.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "outerParentCombineId",
                HttpResultCode.DEPT_OUTER_PARENT_ID_NULL.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                HttpResultCode.DEPT_NAME_NULL.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayOrder",
                HttpResultCode.DEPT_DISPLAY_ORDER_NULL.getCode());
    }
}
