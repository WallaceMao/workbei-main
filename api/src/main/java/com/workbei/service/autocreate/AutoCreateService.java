package com.workbei.service.autocreate;

import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 14:44
 */
public interface AutoCreateService {
    void createTeam(AutoCreateTeamVO teamVO);
    void createUser(AutoCreateUserVO userVO);
    void updateUser(AutoCreateUserVO userVO);
    void updateUserLeaveTeam(AutoCreateUserVO userVO);
    void updateUserSetAdmin(AutoCreateUserVO userVO);

    void createDepartment(AutoCreateDepartmentVO departmentVO);
    void updateDepartment(AutoCreateDepartmentVO departmentVO);
    void deleteDepartment(AutoCreateDepartmentVO departmentVO);
}
