package com.workbei.dao.user;

import com.workbei.model.domain.user.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbTeamDao")
public interface WbTeamDao {

    void saveOrUpdateTeam(WbTeamDO teamDO);
    WbTeamDO getTeamById(@Param("id") Long id);

    void saveOrUpdateTeamData(WbTeamDataDO teamDataDO);
    WbTeamDataDO getTeamDataByTeamId(@Param("teamId") Long teamId);

    void saveOrUpdateTeamUser(WbTeamUserDO teamUserDO);
    WbTeamUserDO getTeamUserByTeamId(@Param("teamId") Long teamId);

    void saveOrUpdateTeamUserRole(WbTeamUserRoleDO teamUserRoleDO);
    void deleteTeamUserRoleByTeamIdAndUserId(
            @Param("teamId") Long teamId,
            @Param("userId") Long userId);
    void deleteTeamUserRoleByTeamIdAndUserIdAndRole(
            @Param("teamId") Long teamId,
            @Param("userId") Long userId,
            @Param("role") String role);
    List<WbTeamUserRoleDO> listTeamUserRoleByTeamIdAndUserId(
            @Param("teamId") Long teamId,
            @Param("userId") Long userId);
    WbTeamUserRoleDO getTeamUserRoleByTeamIdAndUserIdAndRole(
            @Param("teamId") Long teamId,
            @Param("userId") Long userId,
            @Param("role") String role);

    void saveOrUpdateJoinAndQuitTeamRecord(WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO);
    List<WbJoinAndQuitTeamRecordDO> listJoinAndQuitTeamRecordByTeamIdAndUserId(
            @Param("teamId") Long teamId,
            @Param("userId") Long userId);
    WbJoinAndQuitTeamRecordDO getJoinAndQuitTeamRecordByTeamIdAndUserIdAndType(
            @Param("teamId") Long teamId,
            @Param("userId") Long userId,
            @Param("type") String type);

}
