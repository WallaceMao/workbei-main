package com.workbei.dao.user;

import com.workbei.model.domain.user.WbRoleGroupDO;
import com.workbei.model.domain.user.WbUserRoleGroupDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 15:53
 */
@Repository("wbRoleDao")
public interface WbRoleDao {
    WbRoleGroupDO getRoleGroupByName(@Param("name") String name);

    void saveOrUpdateUserRoleGroup(WbUserRoleGroupDO userRoleGroupDO);
}
