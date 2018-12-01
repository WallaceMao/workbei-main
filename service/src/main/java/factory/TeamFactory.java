package factory;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 10:26
 */
public class TeamFactory {
    public static WbTeamDO getTeam(){
        WbTeamDO teamDO = new WbTeamDO();
        teamDO.setLogo(WbConstant.TEAM_DEFAULT_LOGO);
        teamDO.setDisplay(true);
        return teamDO;
    }

    public static WbTeamDataDO getTeamData(){
        WbTeamDataDO teamDataDO = new WbTeamDataDO();
        teamDataDO.setIndustry("1");
        teamDataDO.setCanTel(true);
        return teamDataDO;
    }

    public static WbTeamUserDO getTeamUser(){
        WbTeamUserDO teamUserDO = new WbTeamUserDO();
        teamUserDO.setWhoCanInviteColleague(WbConstant.TEAM_WHO_CAN_INVITE_ALL);
        return teamUserDO;
    }

    public static WbTeamUserRoleDO getTeamUserRole(){
        return new WbTeamUserRoleDO();
    }

    public static WbOuterDataTeamDO getOuterDataTeam(){
        return new WbOuterDataTeamDO();
    }

    public static WbJoinAndQuitTeamRecordDO getJoinAndQuitTeamRecord(){
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = new WbJoinAndQuitTeamRecordDO();
        return joinAndQuitTeamRecordDO;
    }
}
