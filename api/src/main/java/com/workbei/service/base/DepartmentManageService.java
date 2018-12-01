package com.workbei.service.base;

import com.workbei.model.domain.user.WbDepartmentDO;
import com.workbei.model.domain.user.WbOuterDataDepartmentDO;
import com.workbei.model.domain.user.WbTeamDO;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface DepartmentManageService {
    WbOuterDataDepartmentDO getOuterDataDepartmentClientAndOuterId(String client, String outerCorpId);

    Long getOuterDataDepartmentDepartmentIdByClientAndOuterId(String client, String outerId);

    WbDepartmentDO getDepartmentById(Long id);

    void saveDepartmentInfo(AutoCreateDepartmentVO departmentVO, WbTeamDO teamDO);

    void saveTeamDefaultDepartment(WbTeamDO teamDO);

    WbDepartmentDO getTeamUnassignedDepartment(Long teamId);

    void saveDepartmentUser(Long departmentId, Long userId);

    WbDepartmentDO getDepartmentByOuterId(String client, String outerId);

    List<Long> listUserDeptDepartmentIdByUser(Long userId);

    void removeDepartmentUser(Long departmengId, Long userId);

    void updateDepartmentInfo(AutoCreateDepartmentVO departmentVO, WbDepartmentDO departmentDO);

    void deleteDepartmentInfo(WbDepartmentDO departmentDO);
}
