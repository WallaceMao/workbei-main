package com.workbei.manager.user;

import com.workbei.model.domain.user.WbDepartmentDO;
import com.workbei.model.domain.user.WbOuterDataDepartmentDO;
import com.workbei.model.domain.user.WbUserDeptAscriptionDO;
import com.workbei.model.domain.user.WbUserDeptDO;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface DepartmentManager {

    void saveOrUpdateDepartment(WbDepartmentDO commonDepartment);

    void saveOrUpdateOuterDataDepartment(WbOuterDataDepartmentDO outerDataDepartmentDO);

    WbOuterDataDepartmentDO getOuterDataDepartmentByClientAndOuterId(String client, String outerId);

    Long getOuterDataDepartmentDepartmentIdByClientAndOuterId(String client, String outerId);

    void saveOrUpdateDepartment(WbDepartmentDO department, String parentCode);

    WbDepartmentDO getDepartmentById(Long id);

    void saveDepartmentInfo(Long teamId, AutoCreateDepartmentVO departmentVO);

    void saveOrUpdateUserDeptAscription(WbUserDeptAscriptionDO userDeptAscriptionDO);

    WbUserDeptAscriptionDO getUserDeptAscriptionByDepartmentIdAndUserId(Long departmentId, Long userId);

    void saveTeamDefaultDepartment(Long teamId, String topDepartmentName);

    WbDepartmentDO getTeamUnassignedDepartment(Long teamId);

    void saveDepartmentUser(Long departmentId, Long userId);

    WbDepartmentDO getTeamTopDepartment(Long teamId);

    WbDepartmentDO getDepartmentByClientAndOuterId(String client, String outerId);

    List<WbDepartmentDO> listDepartmentByTeamId(Long teamId);

    void saveOrUpdateUserDept(WbUserDeptDO userDeptDO);

    WbUserDeptDO getUserDeptByDepartmentIdAndUserId(Long departmentId, Long userId);

    List<Long> listUserDeptDepartmentIdByUserId(Long userId);

    void deleteDepartmentUser(Long departmentId, Long userId);

    void updateDepartmentInfo(AutoCreateDepartmentVO departmentVO);

    void deleteDepartmentInfo(AutoCreateDepartmentVO departmentVO);

    List<Long> listUserDeptAscriptionDepartmentIdByUserId(Long userId);

    List<Long> listUserDeptAscriptionUserIdByDepartmentId(Long departmentId);
}
