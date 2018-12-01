package com.workbei.service.base;

import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.model.domain.user.WbTeamDO;
import com.workbei.model.domain.user.WbUserDO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:52
 */
public interface TeamManageService {
    WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(String client, String outerCorpIdId);

    WbTeamDO getTeamById(Long teamId);

    WbTeamDO saveTeamInfo(AutoCreateTeamVO teamVO);

    void saveTeamUser(WbTeamDO teamDO, WbUserDO userDO, String teamUserRole, String joinType);

    void removeTeamUser(Long teamId, Long userId);

    void updateAdmin(WbUserDO userDO, Boolean admin);
}
