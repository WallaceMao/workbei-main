package com.workbei.dao.user;

import com.workbei.model.domain.user.WbDepartmentDO;
import com.workbei.model.domain.user.WbUserDeptAscriptionDO;
import com.workbei.model.domain.user.WbUserDeptDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbDepartmentDao")
public interface WbDepartmentDao {
    void saveOrUpdateDepartment(WbDepartmentDO department);
    void deleteDepartmentById(
            @Param("id") Long id);
    List<WbDepartmentDO> listDepartmentByTeamId(
            @Param("teamId") Long teamId);
    WbDepartmentDO getDepartmentById(
            @Param("id") Long id);
    WbDepartmentDO getTopDepartment(
            @Param("teamId") Long teamId);
    Long getTopDepartmentId(
            @Param("teamId") Long teamId);
    WbDepartmentDO getUnassignedDepartment(
            @Param("teamId") Long teamId);
    List<WbDepartmentDO> getDepartmentListByIds(
            @Param("ids") String[] ids);
    WbDepartmentDO getDepartmentByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerParentCombineId);
    List<WbDepartmentDO> listDepartmentByParentId(
            @Param("parentId") Long parentId);

    void saveOrUpdateUserDept(WbUserDeptDO userDeptDO);
    void updateUserDeptDepartmentId(
            @Param("sourceId") Long sourceDepartmentId,
            @Param("targetId") Long targetDepartmentId);
    void deleteUserDeptByDepartmentId(
            @Param("departmentId") Long departmentId);
    void deleteUserDeptByDepartmentIdAndUserId(
            @Param("departmentId") Long departmentId,
            @Param("userId") Long userId);
    WbUserDeptDO getUserDeptByDepartmentIdAndUserId(
            @Param("departmentId") Long departmentId,
            @Param("userId") Long userId);
    List<Long> listUserDeptDepartmentIdByUserId(
            @Param("userId") Long id);
    List<Long> listUserDeptUserIdByDepartmentId(
            @Param("departmentId") Long departmentId);
    List<WbUserDeptDO> listUserDeptByUserId(
            @Param("userId") Long userId);
    List<Long> listUserUserIdWithoutDepartment(
            @Param("teamId") Long teamId);

    void saveOrUpdateUserDeptAscription(WbUserDeptAscriptionDO userDeptAscriptionDO);
    void deleteUserDeptAscriptionByDepartmentIdAndUserId(
            @Param("departmentId") Long departmentId,
            @Param("userId") Long userId);
    void deleteUserDeptAscriptionByDepartmentId(
            @Param("departmentId") Long deptId);
    void deleteUserDeptAscriptionByUserIdAndDepartmentType(
            @Param("userId") Long userId,
            @Param("departmentType") String departmentType);
    void deleteUserDeptAscriptionByUserId(
            @Param("userId") Long userId);
    WbUserDeptAscriptionDO getUserDeptAscriptionByDepartmentIdAndUserId(
            @Param("departmentId") Long departmentId,
            @Param("userId") Long userId);
    List<Long> listUserDeptAscriptionUserIdByDepartmentId(
            @Param("departmentId") Long deptId);
    List<Long> listUserDeptAscriptionDepartmentIdByUserId(
            @Param("userId") Long userId);
}
