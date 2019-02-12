package com.workbei.manager.user.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbOuterDataTeamDao;
import com.workbei.dao.user.WbTeamDao;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.manager.user.TeamManager;
import com.workbei.factory.TeamFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * team聚合，
 * 包括的domain:
 * WbTeamDO
 * WbTeamDataDO
 * WbTeamUserDO
 * WbOuterDataTeamDO
 * 负责管理的关联:
 * WbTeamUserRoleDO
 * WbJoinAndQuitTeamRecordDO
 *
 * @author Wallace Mao
 * Date: 2018-11-27 15:53
 */
public class TeamManagerImpl implements TeamManager {
    private WbTeamDao wbTeamDao;
    private WbOuterDataTeamDao wbOuterDataTeamDao;

    public TeamManagerImpl(WbTeamDao wbTeamDao, WbOuterDataTeamDao wbOuterDataTeamDao) {
        this.wbTeamDao = wbTeamDao;
        this.wbOuterDataTeamDao = wbOuterDataTeamDao;
    }

    //  --------team--------
    @Override
    public WbTeamDO saveOrUpdateTeam(WbTeamDO teamDO) {
        wbTeamDao.saveOrUpdateTeam(teamDO);
        return teamDO;
    }

    @Override
    public WbTeamDO getTeamById(Long teamId) {
        return wbTeamDao.getTeamById(teamId);
    }

    @Override
    public WbTeamDO getTeamByClientAndOuterId(String client, String outerCorpId) {
        return wbTeamDao.getTeamByClientAndOuterId(client, outerCorpId);
    }

    //  --------teamData--------
    @Override
    public WbTeamDataDO getTeamDataByTeamId(Long teamId) {
        return wbTeamDao.getTeamDataByTeamId(teamId);
    }

    //  --------teamUser--------
    @Override
    public WbTeamUserDO getTeamUserByTeamId(Long teamId) {
        return wbTeamDao.getTeamUserByTeamId(teamId);
    }

    //  --------teamUserRole--------
    @Override
    public WbTeamUserRoleDO getTeamUserRoleByTeamIdAndUserIdAndRole(Long teamId, Long userId, String role) {
        return wbTeamDao.getTeamUserRoleByTeamIdAndUserIdAndRole(teamId, userId, role);
    }

    @Override
    public List<WbTeamUserRoleDO> listTeamUserRoleByTeamIdAndUserId(Long teamId, Long userId) {
        return wbTeamDao.listTeamUserRoleByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public List<WbTeamUserRoleDO> listTeamUserRoleByTeamIdAndRole(Long teamId, String role) {
        return wbTeamDao.listTeamUserRoleByTeamIdAndRole(teamId, role);
    }

    //  --------outerDataTeam--------
    @Override
    public WbOuterDataTeamDO saveOrUpdateOuterDataTeam(WbOuterDataTeamDO outerDataTeamDO) {
        wbOuterDataTeamDao.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        return outerDataTeamDO;
    }

    @Override
    public WbOuterDataTeamDO getOuterDataTeamByClientAndOuterId(String client, String outerCorpId) {
        return wbOuterDataTeamDao.getOuterDataTeamByClientAndOuterId(client, outerCorpId);
    }

    //  --------joinAndQuiteTeamRecord--------
    @Override
    public List<WbJoinAndQuitTeamRecordDO> listJoinAndQuitTeamRecordByTeamIdAndUserId(Long teamId, Long userId) {
        return wbTeamDao.listJoinAndQuitTeamRecordByTeamIdAndUserId(teamId, userId);
    }

    //  --------aggregate method--------

    /**
     * 保存team的基本信息，同时也会跟team相关的聚合类
     *
     * @param teamVO
     * @return
     */
    @Override
    public WbTeamDO saveTeamInfo(AutoCreateTeamVO teamVO) {
        //  保存公司
        WbTeamDO teamDO = TeamFactory.getTeamDO();
        teamDO.setName(teamVO.getName());
        if (teamVO.getLogo() != null) {
            teamDO.setLogo(teamVO.getLogo());
        }
        wbTeamDao.saveOrUpdateTeam(teamDO);
        Long teamId = teamDO.getId();
        //  保存公司的属性
        WbTeamDataDO teamDataDO = TeamFactory.getTeamDataDO();
        teamDataDO.setTeamId(teamId);
        teamDataDO.setClient(teamVO.getClient());
        String contactName = WbConstant.APP_DEFAULT_TEAM_CONTACT_NAME;
        if (teamVO.getCreator() != null && teamVO.getCreator().getName() != null) {
            contactName = teamVO.getCreator().getName();
        }
        teamDataDO.setContacts(contactName);
        wbTeamDao.saveOrUpdateTeamData(teamDataDO);
        WbTeamUserDO teamUserDO = TeamFactory.getTeamUserDO();
        teamUserDO.setTeamId(teamId);
        wbTeamDao.saveOrUpdateTeamUser(teamUserDO);
        if (teamVO.getOuterCorpId() != null) {
            WbOuterDataTeamDO outerDataTeamDO = TeamFactory.getOuterDataTeamDO();
            outerDataTeamDO.setTeamId(teamDO.getId());
            outerDataTeamDO.setClient(teamVO.getClient());
            outerDataTeamDO.setOuterId(teamVO.getOuterCorpId());
            wbOuterDataTeamDao.saveOrUpdateOuterDataTeam(outerDataTeamDO);
        }

        return teamDO;
    }

    /**
     * 新增团队创建者的角色
     *
     * @param teamId
     * @param userId
     */
    @Override
    public WbTeamUserRoleDO saveTeamCreatorRole(Long teamId, Long userId) {
        WbTeamUserRoleDO creatorRole =saveOnlyTeamCreator(teamId, userId);
        saveOnlyTeamAdmin(teamId, userId);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecordDO();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_RECORD_TYPE_CREATE);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);

        return creatorRole;
    }

    /**
     * 新增普通用户的团队角色
     *
     * @param teamId
     * @param userId
     */
    @Override
    public WbTeamUserRoleDO saveTeamCommonUserRole(Long teamId, Long userId) {
        WbTeamUserRoleDO userRole = saveOnlyTeamCommonRole(teamId, userId);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecordDO();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_RECORD_TYPE_JOIN);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);

        return userRole;
    }

    /**
     * 删除团队用户的角色，同时打出日志
     *
     * @param teamId
     * @param userId
     */
    @Override
    public void deleteTeamUserRole(Long teamId, Long userId) {
        //  删除teamUserRole
        wbTeamDao.deleteTeamUserRoleByTeamIdAndUserId(teamId, userId);
        //  添加创建公司的记录
        WbJoinAndQuitTeamRecordDO joinAndQuitTeamRecordDO = TeamFactory.getJoinAndQuitTeamRecordDO();
        joinAndQuitTeamRecordDO.setTeamId(teamId);
        joinAndQuitTeamRecordDO.setUserId(userId);
        joinAndQuitTeamRecordDO.setType(WbConstant.TEAM_RECORD_TYPE_QUIT);
        wbTeamDao.saveOrUpdateJoinAndQuitTeamRecord(joinAndQuitTeamRecordDO);
    }

    /**
     * 更新团队的管理员角色
     *
     * @param teamId
     * @param userId
     * @param admin
     */
    @Override
    public WbTeamUserRoleDO updateTeamAdmin(Long teamId, Long userId, Boolean admin) {
        WbTeamUserRoleDO adminRole = wbTeamDao.getTeamUserRoleByTeamIdAndUserIdAndRole(
                teamId,
                userId,
                WbConstant.TEAM_USER_ROLE_ADMIN);
        if (admin) {
            if (adminRole == null) {
                saveOnlyTeamAdmin(teamId, userId);
            }
        } else {
            if (adminRole != null) {
                wbTeamDao.deleteTeamUserRoleByTeamIdAndUserIdAndRole(
                        teamId,
                        userId,
                        WbConstant.TEAM_USER_ROLE_ADMIN
                );
                adminRole = null;
            }
        }
        return adminRole;
    }

    /**
     * 批量修改团队管理员，这里的操作步骤为：
     * 1.  读取当前的admin的id的列表，currentAdminIdList
     * 2.  比对currentAdminIdList和adminIdList
     * 3.  adminIdList中存在，currentAdminIdList不存在，那么新增admin
     * 4.  adminIdList中不存在，currentAdminIdList中存在，那么删除原admin
     * @param teamId
     * @param adminIdList
     */
    @Override
    public void updateBatchTeamAdmin(Long teamId, List<Long> adminIdList) {
        List<Long> currentAdminList = wbTeamDao.listTeamUserRoleUserIdByTeamIdAndRole(
                teamId,
                WbConstant.TEAM_USER_ROLE_ADMIN);
        List<Long> insertList = findExcludeElement(adminIdList, currentAdminList);
        List<Long> deleteList = findExcludeElement(currentAdminList, adminIdList);
        for (Long insertId : insertList) {
            saveOnlyTeamAdmin(teamId, insertId);
        }
        for (Long deleteId : deleteList) {
            wbTeamDao.deleteTeamUserRoleByTeamIdAndUserIdAndRole(
                    teamId,
                    deleteId,
                    WbConstant.TEAM_USER_ROLE_ADMIN);
        }
    }

    private List<Long> findExcludeElement(List<Long> iterateList, List<Long> compareList) {
        List<Long> list = new ArrayList<>();
        for (Long adminId : iterateList) {
            if (!compareList.contains(adminId)) {
                list.add(adminId);
            }
        }
        return list;
    }

    private WbTeamUserRoleDO saveOnlyTeamCommonRole(Long teamId, Long userId) {
        return saveOnlyTeamUserRole(teamId, userId, WbConstant.TEAM_USER_ROLE_COMMON);
    }

    private WbTeamUserRoleDO saveOnlyTeamAdmin(Long teamId, Long userId) {
        return saveOnlyTeamUserRole(teamId, userId, WbConstant.TEAM_USER_ROLE_ADMIN);
    }

    private WbTeamUserRoleDO saveOnlyTeamCreator(Long teamId, Long userId) {
        return saveOnlyTeamUserRole(teamId, userId, WbConstant.TEAM_USER_ROLE_CREATOR);
    }

    private WbTeamUserRoleDO saveOnlyTeamUserRole(Long teamId, Long userId, String role) {
        //  新增角色
        WbTeamUserRoleDO adminRole = TeamFactory.getTeamUserRoleDO();
        adminRole.setTeamId(teamId);
        adminRole.setUserId(userId);
        adminRole.setRole(role);
        wbTeamDao.saveOrUpdateTeamUserRole(adminRole);
        return adminRole;
    }
}
