package com.workbei.dao.user;

import com.workbei.model.domain.user.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbTeamDao")
public interface WbTeamDao {
    WbTeamDO getTeamById(@Param("id") Long id);
    void saveOrUpdateTeam(WbTeamDO teamDO);
    void saveOrUpdateTeamData(WbTeamDataDO teamDataDO);
    void saveOrUpdateTeamUser(WbTeamUserDO teamUserDO);
    void saveOrUpdateTeamUserRole(WbTeamUserRoleDO teamUserRoleDO);
    void getTeamUserRoleByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
    void deleteTeamUserRoleByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
    void saveOrUpdateJoinAndQuitTeamRecord(WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO);

}
