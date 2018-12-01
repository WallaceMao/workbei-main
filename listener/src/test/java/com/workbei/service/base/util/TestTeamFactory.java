package com.workbei.service.base.util;

import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbTeamDO;
import com.workbei.model.domain.user.WbUserDO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import factory.TeamFactory;
import factory.UserFactory;

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
        team.setClient("c_" + now.getTime() % 10000);
        return team;
    }

    public static WbTeamDO getTeamDO(){
        Date now = new Date();
        WbTeamDO teamDO = TeamFactory.getTeamDO();
        teamDO.setName("auto_test_team_name_" + now.getTime());
        return teamDO;
    }
}
