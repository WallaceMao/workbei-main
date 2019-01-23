package com.workbei.manager.user.impl;

import com.workbei.dao.user.*;
import com.workbei.exception.ExceptionCode;
import com.workbei.exception.WorkbeiServiceException;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.manager.user.UserManager;
import com.workbei.factory.UserFactory;

import java.util.List;

import static com.workbei.exception.ExceptionCode.*;

/**
 * user
 * @author Wallace Mao
 * Date: 2018-11-27 15:53
 */
public class UserManagerImpl implements UserManager {
    private WbUserDao wbUserDao;
    private WbOuterDataUserDao wbOuterDataUserDao;

    public UserManagerImpl(WbUserDao wbUserDao, WbOuterDataUserDao wbOuterDataUserDao) {
        this.wbUserDao = wbUserDao;
        this.wbOuterDataUserDao = wbOuterDataUserDao;
    }

    //  --------user--------
    @Override
    public void saveOrUpdateUser(WbUserDO userDO) {
        wbUserDao.saveOrUpdateUser(userDO);
    }

    @Override
    public List<WbUserDO> listUserByTeamId(Long teamId) {
        return wbUserDao.listUserByTeamId(teamId);
    }

    @Override
    public WbUserDO getUserById(Long id){
        return wbUserDao.getUserById(id);
    }

    @Override
    public WbUserDO getUserByClientAndOuterId(String client, String outerCombineId) {
        return wbUserDao.getUserByClientAndOuterId(client, outerCombineId);
    }

    //  --------userGuide--------
    @Override
    public WbUserGuideDO getUserGuideByUserId(Long userId) {
        return wbUserDao.getUserGuideByUserId(userId);
    }

    //  --------userDisplay-------
    @Override
    public WbUserDisplayOrderDO getUserDisplayOrderByUserId(Long userId) {
        return wbUserDao.getUserDisplayOrderByUserId(userId);
    }

    //  --------userUiSetting--------
    @Override
    public WbUserUiSettingDO getUserUiSettingByUserId(Long userId) {
        return wbUserDao.getUserUiSettingByUserId(userId);
    }

    //  --------userFunctionSetting--------
    @Override
    public WbUserFunctionSettingDO getUserFunctionSettingByUserId(Long userId) {
        return wbUserDao.getUserFunctionSettingByUserId(userId);
    }

    //  --------outerDataUser--------
    @Override
    public WbOuterDataUserDO getOuterDataUserByClientAndOuterId(String client, String outerId){
        return wbOuterDataUserDao.getOuterDataUserByClientAndOuterId(client, outerId);
    }

    @Override
    public WbOuterDataUserDO getOuterDataUserByClientAndUserId(String client, Long userId) {
        return wbOuterDataUserDao.getOuterDataUserByClientAndUserId(client, userId);
    }

    //  --------aggregate method
    /**
     * 逻辑如下：
     * 1  如果params中有unionId，那么首先根据unionId查找是否有相同unionId的Account。
     *    如果用户之前使用钉钉扫码登录过web端，那么可能会出现这种情况。
     * 2  如果没有unionId
     *
     * @param accountId
     * @param userVO
     * @return
     */
    @Override
    public WbUserDO saveUserInfo(Long teamId, Long accountId, AutoCreateUserVO userVO){
        //  如果outerDataUser中存在，那么就不创建，直接返回
        if(teamId == null){
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.TEAM_NOT_FOUND, null, accountId, userVO));
        }
        if(accountId == null){
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ExceptionCode.ACCOUNT_NOT_FOUND, teamId, null, userVO));
        }
        //  新增user
        WbUserDO userDO = UserFactory.getUserDO();
        userDO.setAccountId(accountId);
        userDO.setName(userVO.getName());
        userDO.setTeamId(teamId);

        wbUserDao.saveOrUpdateUser(userDO);
        Long userId = userDO.getId();
        if(userVO.getOuterCombineId() != null){
            WbOuterDataUserDO outerDataUserDO = UserFactory.getOuterDataUserDO();
            outerDataUserDO.setUserId(userDO.getId());
            outerDataUserDO.setOuterId(userVO.getOuterCombineId());
            outerDataUserDO.setClient(userVO.getClient());
            wbOuterDataUserDao.saveOrUpdateOuterDataUser(outerDataUserDO);
        }
        //  保存用户的基本设置
        WbUserGuideDO userGuideDO = UserFactory.getUserGuideDO();
        userGuideDO.setUserId(userId);
        wbUserDao.saveOrUpdateUserGuide(userGuideDO);
        WbUserDisplayOrderDO userDisplayOrderDO = UserFactory.getUserDisplayOrderDO();
        userDisplayOrderDO.setUserId(userId);
        wbUserDao.saveOrUpdateUserDisplayOrder(userDisplayOrderDO);
        WbUserUiSettingDO userUiSettingDO = UserFactory.getUserUiSettingDO();
        userUiSettingDO.setUserId(userId);
        wbUserDao.saveOrUpdateUserUiSetting(userUiSettingDO);
        WbUserFunctionSettingDO userFunctionSettingDO = UserFactory.getUserFunctionSettingDO();
        userFunctionSettingDO.setUserId(userId);
        wbUserDao.saveOrUpdateUserFunctionSetting(userFunctionSettingDO);

        //TODO 生成默认的标签，暂时不做

        return userDO;
    }

    /**
     * 暂时不支持修改ddUnionId
     * @param userVO
     * @return
     */
    @Override
    public WbUserDO updateUserInfo(AutoCreateUserVO userVO) {
        WbUserDO userDO = getUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(userDO == null){
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(USER_NOT_FOUND, userVO));
        }
        WbUserDO updatedUserDO = null;

        if(userVO.getName() != null){
            updatedUserDO = userDO;
            updatedUserDO.setName(userVO.getName());
        }
        //  执行更新
        if(updatedUserDO != null){
            wbUserDao.saveOrUpdateUser(updatedUserDO);
        }
        return userDO;
    }
}
