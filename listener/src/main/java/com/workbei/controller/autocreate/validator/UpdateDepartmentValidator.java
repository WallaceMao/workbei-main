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
@Component("updateDepartmentValidator")
public class UpdateDepartmentValidator implements Validator {
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
        AutoCreateDepartmentVO deptVO = (AutoCreateDepartmentVO) o;
        if(deptVO.getOuterParentCombineId() == null
                && deptVO.getName() == null
                && deptVO.getDisplayOrder() == null) {
            errors.rejectValue("outerParentCombineId", HttpResultCode.DEPT_UPDATE_NO_FIELD.getCode());
        }
    }
}
