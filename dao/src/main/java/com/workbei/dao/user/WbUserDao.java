package com.workbei.dao.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:17
 */
@Repository("wbUserDao")
public interface WbUserDao {
    public void saveOrUpdateUserDeptAscription(
            @Param("departmentId") Long deptId,
            @Param("userId") Long userId);

    public Long getTopDepartment(
            @Param("teamId") Long teamId
    );
}
