package com.workbei.service.base.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbOuterDataTeamDao;
import com.workbei.dao.user.WbTeamDao;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.service.base.TeamManageService;
import factory.TeamFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:53
 */
public class TeamManageServiceImpl implements TeamManageService {
    @Autowired
    private WbTeamDao wbTeamDao;
    @Autowired
    private WbOuterDataTeamDao wbOuterDataTeamDao;

    @Override
    public WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(String client, String outerCorpId){
        return wbOuterDataTeamDao.getOuterDataTeamByClientAndOuterId(client, outerCorpId);
    }

    @Override
    public WbTeamDO getTeamById(Long teamId){
        return wbTeamDao.getTeamById(teamId);
    }

    @Override
    public WbTeamDO saveTeamInfo(AutoCreateTeamVO teamVO) {
        //  保存公司
        WbTeamDO teamDO = TeamFactory.getTeam();
        teamDO.setName(teamVO.getName());
        if(teamVO.getLogo() != null){
            teamDO.setLogo(teamVO.getLogo());
        }
        wbTeamDao.saveOrUpdateTeam(teamDO);
        Long teamId = teamDO.getId();
        //  保存公司的属性
        WbTeamDataDO teamDataDO = TeamFactory.getTeamData();
        teamDataDO.setTeamId(teamId);
        teamDataDO.setClient(teamVO.getClient());
        teamDataDO.setOuterId(teamVO.getOuterCorpId());
        wbTeamDao.saveOrUpdateTeamData(teamDataDO);
        WbTeamUserDO teamUserDO = TeamFactory.getTeamUser();
        teamUserDO.setTeamId(teamId);
        wbTeamDao.saveOrUpdateTeamUser(teamUserDO);
        if(teamVO.getOuterCorpId() != null){
            WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeam();
            outerDataTeamDO.setTeamId(teamDO.getId());
            outerDataTeamDO.setClient(teamVO.getClient());
            outerDataTeamDO.setOuterId(teamVO.getOuterCorpId());
            wbOuterDataTeamDao.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        }

        return teamDO;
    }

    @Override
    public void saveTeamUser(WbTeamDO teamDO, WbUserDO userDO, String teamUserRole, String joinType) {
        //  设置teamUserRole
        WbTeamUserRoleDO teamUserRoleDO = TeamFactory.getTeamUserRole();
        teamUserRoleDO.setTeamId(teamDO.getId());
        teamUserRoleDO.setUserId(userDO.getId());
        teamUserRoleDO.setRole(teamUserRole);
        wbTeamDao.saveOrUpdateTeamUserRole(teamUserRoleDO);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecord();
        joinAndQuitTeamRecordDO.setTeamId(teamDO.getId());
        joinAndQuitTeamRecordDO.setUserId(userDO.getId());
        joinAndQuitTeamRecordDO.setType(joinType);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);
    }

    @Override
    public void removeTeamUser(Long teamId, Long userId) {
        //  删除teamUserRole
        wbTeamDao.deleteTeamUserRoleByTeamIdAndUserId(teamId, userId);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecord();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_USER_ROLE_CREATOR);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);
    }

    @Override
    public void updateAdmin(WbUserDO userDO, Boolean admin) {
        Long teamId = userDO.getTeamId();
        Long userId = userDO.getId();
        //  删除原有的角色
        wbTeamDao.deleteTeamUserRoleByTeamIdAndUserId(teamId, userId);
        //  新增角色
        WbTeamUserRoleDO teamUserRoleDO = TeamFactory.getTeamUserRole();
        teamUserRoleDO.setTeamId(teamId);
        teamUserRoleDO.setUserId(userId);
        teamUserRoleDO.setRole(WbConstant.TEAM_USER_ROLE_ADMIN);
        wbTeamDao.saveOrUpdateTeamUserRole(teamUserRoleDO);
    }
}
