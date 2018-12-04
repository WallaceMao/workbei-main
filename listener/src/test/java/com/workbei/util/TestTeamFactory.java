package com.workbei.util;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.model.domain.user.WbTeamDO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.factory.TeamFactory;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-12-01 17:01
 */
public class TestTeamFactory {
    public static AutoCreateTeamVO getAutoCreateTeamVO(){
        AutoCreateTeamVO team = new AutoCreateTeamVO();
        Date now = new Date();
        team.setName("auto_test_team_name_" + now.getTime());
        team.setClient(WbConstant.APP_DEFAULT_CLIENT);
        return team;
    }

    public static WbTeamDO getTeamDO(){
        Date now = new Date();
        WbTeamDO teamDO = TeamFactory.getTeamDO();
        teamDO.setName("auto_test_team_name_" + now.getTime());
        return teamDO;
    }

    public static WbOuterDataTeamDO getOuterDataTeam(Long teamId){
        Date now = new Date();
        WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeamDO();
        outerDataTeamDO.setClient(WbConstant.APP_DEFAULT_CLIENT);
        outerDataTeamDO.setTeamId(teamId);
        outerDataTeamDO.setOuterId("at_team_outer_id_" + now.getTime());
        return outerDataTeamDO;
    }
}
