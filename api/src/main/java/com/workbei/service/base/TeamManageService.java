package com.workbei.service.base;

import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface TeamManageService {
    WbTeamDataDO getTeamDataByTeamId(Long teamId);

    WbTeamUserDO getTeamUserByTeamId(Long teamId);

    void saveOrUpdateTeam(WbTeamDO teamDO);

    void saveOrUpdateOuterDataTeam(WbOuterDataTeamDO outerDataTeamDO);

    WbTeamDO getTeamById(Long teamId);

    WbTeamUserRoleDO getTeamUserRoleByTeamIdAndUserIdAndRole(Long teamId, Long userId, String role);

    List<WbTeamUserRoleDO> listTeamUserRoleByTeamIdAndUserId(Long teamId, Long userId);

    List<WbJoinAndQuitTeamRecordDO> listJoinAndQuitTeamRecordByTeamIdAndUserId(Long teamId, Long userId);

    WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(String client, String outerCorpId);

    WbTeamDO saveTeamInfo(AutoCreateTeamVO teamVO);

    void saveTeamCreator(Long teamId, Long userId);

    void saveTeamCommonUser(Long teamId, Long userId);

    void removeTeamUser(Long teamId, Long userId);

    void updateTeamAdmin(Long teamId, Long userId, Boolean admin);
}
