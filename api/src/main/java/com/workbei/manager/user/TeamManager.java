package com.workbei.manager.user;

import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface TeamManager {
    //  --------team--------
    WbTeamDO saveOrUpdateTeam(WbTeamDO teamDO);

    WbTeamDO getTeamById(Long teamId);

    WbTeamDO getTeamByClientAndOuterId(String client, String outerCorpId);

    //  --------teamData--------
    WbTeamDataDO getTeamDataByTeamId(Long teamId);

    //  --------teamUser--------
    WbTeamUserDO getTeamUserByTeamId(Long teamId);

    //  --------teamUserRole--------
    WbTeamUserRoleDO getTeamUserRoleByTeamIdAndUserIdAndRole(Long teamId, Long userId, String role);

    List<WbTeamUserRoleDO> listTeamUserRoleByTeamIdAndUserId(Long teamId, Long userId);

    //  --------outerDataTeam--------
    WbOuterDataTeamDO saveOrUpdateOuterDataTeam(WbOuterDataTeamDO outerDataTeamDO);

    WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(String client, String outerCorpId);

    //  --------joinAndQuiteTeamRecord--------
    List<WbJoinAndQuitTeamRecordDO> listJoinAndQuitTeamRecordByTeamIdAndUserId(Long teamId, Long userId);

    //  --------aggregate method--------
    WbTeamDO saveTeamInfo(AutoCreateTeamVO teamVO);

    WbTeamUserRoleDO saveTeamCreatorRole(Long teamId, Long userId);

    WbTeamUserRoleDO saveTeamCommonUserRole(Long teamId, Long userId);

    void deleteTeamUserRole(Long teamId, Long userId);

    WbTeamUserRoleDO updateTeamAdmin(Long teamId, Long userId, Boolean admin);
}
