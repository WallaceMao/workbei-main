package com.workbei.controller.autocreate.validator;

import com.workbei.http.HttpResultCode;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 20:52
 */
@Component("createTeamValidator")
public class CreateTeamValidator implements Validator{
    @Override
    public boolean supports(Class<?> aClass) {
        return AutoCreateTeamVO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "outerCorpId",
                HttpResultCode.TEAM_OUTER_CORP_ID_NULL.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                HttpResultCode.TEAM_NAME_NULL.getCode());
    }
}