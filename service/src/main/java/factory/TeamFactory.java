package factory;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;
import com.workbei.util.UuidUtil;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 10:26
 */
public class TeamFactory {
    public static WbTeamDO getTeamDO(){
        WbTeamDO teamDO = new WbTeamDO();
        teamDO.setUuid(UuidUtil.generateTeamUuid());
        teamDO.setLogo(WbConstant.TEAM_DEFAULT_LOGO);
        teamDO.setDisplay(true);
        return teamDO;
    }

    public static WbTeamDataDO getTeamDataDO(){
        WbTeamDataDO teamDataDO = new WbTeamDataDO();
        teamDataDO.setIndustry(WbConstant.APP_DEFAULT_TEAM_INDUSTRY);
        teamDataDO.setCanTel(WbConstant.APP_DEFAULT_TEAM_CAN_TEL);
        return teamDataDO;
    }

    public static WbTeamUserDO getTeamUserDO(){
        WbTeamUserDO teamUserDO = new WbTeamUserDO();
        teamUserDO.setWhoCanInviteColleague(WbConstant.TEAM_WHO_CAN_INVITE_ALL);
        return teamUserDO;
    }

    public static WbTeamUserRoleDO getTeamUserRoleDO(){
        return new WbTeamUserRoleDO();
    }

    public static WbOuterDataTeamDO getOuterDataTeamDO(){
        return new WbOuterDataTeamDO();
    }

    public static WbJoinAndQuitTeamRecordDO getJoinAndQuitTeamRecordDO(){
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = new WbJoinAndQuitTeamRecordDO();
        return joinAndQuitTeamRecordDO;
    }
}
