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
            @Param("id") Long id
    );
    void saveOrUpdateUserDept(WbUserDeptDO userDeptDO);
    void deleteUserDeptByUserIdAndDepartmentId(
            @Param("userId") Long userId,
            @Param("departmentId") Long deptId
    );
    void saveOrUpdateUserDeptAscription(WbUserDeptAscriptionDO userDeptAscriptionDO);
    void deleteUserDeptAscriptionByUserIdAndDepartmentId(
            @Param("userId") Long userId,
            @Param("departmentId") Long deptId
    );
    WbDepartmentDO getDepartmentById(
            @Param("id") Long id
    );
    WbDepartmentDO getTopDepartment(
            @Param("teamId") Long teamId
    );
    Long getTopDepartmentId(
            @Param("teamId") Long teamId
    );
    WbDepartmentDO getUnassignedDepartment(
            @Param("teamId") Long teamId
    );
    List<WbDepartmentDO> getDepartmentListByIds(@Param("ids") String[] ids);

    List<Long> listUserDeptDepartmentIdByUserId(@Param("userId") Long id);

    WbDepartmentDO getDepartmentByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerParentCombineId);

    List<WbDepartmentDO> listDepartmentsByParentId(@Param("parentId") Long parentId);

    void updateUserDeptDepartmentId(
            @Param("sourceId") Long sourceDepartmentId,
            @Param("targetId") Long targetDepartmentId);

    void deleteUserDeptAscriptionByDepartmentId(
            @Param("departmentId") Long deptId
    );

    List<Long> listUserDeptAscriptionUserIdByDepartmentId(
            @Param("departmentId") Long deptId);
}
