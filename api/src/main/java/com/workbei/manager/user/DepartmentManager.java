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
    //  --------department--------
    WbDepartmentDO saveOrUpdateDepartment(WbDepartmentDO department);

    WbDepartmentDO saveOrUpdateDepartment(WbDepartmentDO department, String parentCode);

    WbDepartmentDO getDepartmentById(Long id);

    WbDepartmentDO getDepartmentByClientAndOuterId(String client, String outerId);

    WbDepartmentDO getTeamTopDepartment(Long teamId);

    WbDepartmentDO getTeamUnassignedDepartment(Long teamId);

    List<WbDepartmentDO> listDepartmentByTeamId(Long teamId);

    //  --------outerDataDepartment--------
    WbOuterDataDepartmentDO saveOrUpdateOuterDataDepartment(WbOuterDataDepartmentDO outerDataDepartmentDO);

    WbOuterDataDepartmentDO getOuterDataDepartmentByClientAndOuterId(String client, String outerId);

    WbOuterDataDepartmentDO getOuterDataDepartmentByClientAndDepartmentId(String client, Long departmentId);

    Long getOuterDataDepartmentDepartmentIdByClientAndOuterId(String client, String outerId);

    //  --------userDept--------
    WbUserDeptDO saveOrUpdateUserDept(WbUserDeptDO userDeptDO);

    WbUserDeptDO getUserDeptByDepartmentIdAndUserId(Long departmentId, Long userId);

    List<Long> listUserDeptDepartmentIdByUserId(Long userId);

    List<Long> listUserDeptUserIdByDepartmentId(Long departmentId);

    List<WbUserDeptDO> listUserDeptByUserId(Long userId);

    //  --------userDeptAscription--------
    WbUserDeptAscriptionDO saveOrUpdateUserDeptAscription(WbUserDeptAscriptionDO userDeptAscriptionDO);

    WbUserDeptAscriptionDO getUserDeptAscriptionByDepartmentIdAndUserId(Long departmentId, Long userId);

    List<Long> listUserDeptAscriptionDepartmentIdByUserId(Long userId);

    List<Long> listUserDeptAscriptionUserIdByDepartmentId(Long departmentId);

    WbDepartmentDO saveTeamDefaultDepartment(Long teamId, String topDepartmentName);

    WbDepartmentDO saveDepartmentInfo(Long teamId, AutoCreateDepartmentVO departmentVO);

    WbDepartmentDO updateDepartmentInfo(AutoCreateDepartmentVO departmentVO);

    void deleteDepartmentInfo(AutoCreateDepartmentVO departmentVO);

    WbUserDeptDO saveDepartmentUser(Long departmentId, Long userId);

    void deleteDepartmentUser(Long departmentId, Long userId);
}
