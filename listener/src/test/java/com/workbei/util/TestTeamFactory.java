package com.workbei.util;

import com.workbei.constant.TestV2Constant;
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
    private static final Long MILLS_DELAY = 1L;

    public static AutoCreateTeamVO getAutoCreateTeamVO() throws Exception{
        Thread.sleep(MILLS_DELAY);
        AutoCreateTeamVO team = new AutoCreateTeamVO();
        Date now = new Date();
        team.setName("auto_test_team_name_" + now.getTime());
        team.setClient(TestV2Constant.CLIENT_FAKED);
        return team;
    }

    public static WbTeamDO getTeamDO() throws InterruptedException {
        Thread.sleep(MILLS_DELAY);
        Date now = new Date();
        WbTeamDO teamDO = TeamFactory.getTeamDO();
        teamDO.setName("auto_test_team_name_" + now.getTime());
        return teamDO;
    }

    public static WbOuterDataTeamDO getOuterDataTeam(Long teamId) throws InterruptedException {
        Thread.sleep(MILLS_DELAY);
        Date now = new Date();
        WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeamDO();
        outerDataTeamDO.setClient(TestV2Constant.CLIENT_FAKED);
        outerDataTeamDO.setTeamId(teamId);
        outerDataTeamDO.setOuterId("at_team_outer_id_" + now.getTime());
        return outerDataTeamDO;
    }
}
