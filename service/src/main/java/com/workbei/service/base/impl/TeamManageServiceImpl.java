package com.workbei.service.base.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbOuterDataTeamDao;
import com.workbei.dao.user.WbTeamDao;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.service.base.TeamManageService;
import factory.TeamFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public void saveOrUpdateTeam(WbTeamDO teamDO){
        wbTeamDao.saveOrUpdateTeam(teamDO);
    }

    @Override
    public WbTeamDO getTeamById(Long teamId){
        return wbTeamDao.getTeamById(teamId);
    }

    @Override
    public WbTeamDataDO getTeamDataByTeamId(Long teamId){
        return wbTeamDao.getTeamDataByTeamId(teamId);
    }

    @Override
    public WbTeamUserDO getTeamUserByTeamId(Long teamId){
        return wbTeamDao.getTeamUserByTeamId(teamId);
    }

    @Override
    public WbTeamUserRoleDO getTeamUserRoleByTeamIdAndUserIdAndRole(Long teamId, Long userId, String role){
        return wbTeamDao.getTeamUserRoleByTeamIdAndUserIdAndRole(teamId, userId, role);
    }

    @Override
    public List<WbTeamUserRoleDO> listTeamUserRoleByTeamIdAndUserId(Long teamId, Long userId){
        return wbTeamDao.listTeamUserRoleByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public List<WbJoinAndQuitTeamRecordDO> listJoinAndQuitTeamRecordByTeamIdAndUserId(Long teamId, Long userId){
        return wbTeamDao.listJoinAndQuitTeamRecordByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(String client, String outerCorpId){
        return wbOuterDataTeamDao.getOuterDataTeamByClientAndOuterId(client, outerCorpId);
    }

    @Override
    public WbTeamDO saveTeamInfo(AutoCreateTeamVO teamVO) {
        //  保存公司
        WbTeamDO teamDO = TeamFactory.getTeamDO();
        teamDO.setName(teamVO.getName());
        if(teamVO.getLogo() != null){
            teamDO.setLogo(teamVO.getLogo());
        }
        wbTeamDao.saveOrUpdateTeam(teamDO);
        Long teamId = teamDO.getId();
        //  保存公司的属性
        WbTeamDataDO teamDataDO = TeamFactory.getTeamDataDO();
        teamDataDO.setTeamId(teamId);
        teamDataDO.setClient(teamVO.getClient());
        String contactName = WbConstant.APP_DEFAULT_TEAM_CONTACT_NAME;
        if(teamVO.getCreator() != null && teamVO.getCreator().getName() != null){
            contactName = teamVO.getCreator().getName();
        }
        teamDataDO.setContacts(contactName);
        wbTeamDao.saveOrUpdateTeamData(teamDataDO);
        WbTeamUserDO teamUserDO = TeamFactory.getTeamUserDO();
        teamUserDO.setTeamId(teamId);
        wbTeamDao.saveOrUpdateTeamUser(teamUserDO);
        if(teamVO.getOuterCorpId() != null){
            WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeamDO();
            outerDataTeamDO.setTeamId(teamDO.getId());
            outerDataTeamDO.setClient(teamVO.getClient());
            outerDataTeamDO.setOuterId(teamVO.getOuterCorpId());
            wbOuterDataTeamDao.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        }

        return teamDO;
    }

    @Override
    public void saveTeamCreator(Long teamId, Long userId) {
        //  设置teamUserRole
        WbTeamUserRoleDO creatorRole = TeamFactory.getTeamUserRoleDO();
        creatorRole.setTeamId(teamId);
        creatorRole.setUserId(userId);
        creatorRole.setRole(WbConstant.TEAM_USER_ROLE_CREATOR);
        wbTeamDao.saveOrUpdateTeamUserRole(creatorRole);
        WbTeamUserRoleDO adminRole = TeamFactory.getTeamUserRoleDO();
        adminRole.setTeamId(teamId);
        adminRole.setUserId(userId);
        adminRole.setRole(WbConstant.TEAM_USER_ROLE_ADMIN);
        wbTeamDao.saveOrUpdateTeamUserRole(adminRole);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecordDO();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_RECORD_TYPE_CREATE);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);
    }

    @Override
    public void saveTeamCommonUser(Long teamId, Long userId){
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecordDO();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_RECORD_TYPE_JOIN);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);
    }

    @Override
    public void removeTeamUser(Long teamId, Long userId) {
        //  删除teamUserRole
        wbTeamDao.deleteTeamUserRoleByTeamIdAndUserId(teamId, userId);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecordDO();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_RECORD_TYPE_QUIT);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);
    }

    @Override
    public void updateTeamAdmin(Long teamId, Long userId, Boolean admin) {
        WbTeamUserRoleDO adminRole = wbTeamDao.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId,
                userId,
                WbConstant.TEAM_USER_ROLE_ADMIN);
        if(admin){
            if(adminRole == null){
                //  新增角色
                WbTeamUserRoleDO teamUserRoleDO = TeamFactory.getTeamUserRoleDO();
                teamUserRoleDO.setTeamId(teamId);
                teamUserRoleDO.setUserId(userId);
                teamUserRoleDO.setRole(WbConstant.TEAM_USER_ROLE_ADMIN);
                wbTeamDao.saveOrUpdateTeamUserRole(teamUserRoleDO);
            }
        }else{
            if(adminRole != null){
                wbTeamDao.deleteTeamUserRoleByTeamIdAndUserIdAndRole(
                        teamId,
                        userId,
                        WbConstant.TEAM_USER_ROLE_ADMIN
                );
            }
        }
    }
}
