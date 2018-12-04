package com.workbei.service.base.impl;

import com.workbei.dao.user.*;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.base.UserManageService;
import com.workbei.factory.UserFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:53
 */
public class UserManageServiceImpl implements UserManageService {
    private static final Logger logger = LoggerFactory.getLogger(UserManageServiceImpl.class);

    @Autowired
    private WbUserDao wbUserDao;
    @Autowired
    private WbOuterDataUserDao wbOuterDataUserDao;
    @Autowired
    private WbAccountDao wbAccountDao;

    @Override
    public void saveOrUpdateUser(WbUserDO userDO) {
        wbUserDao.saveOrUpdateUser(userDO);
    }

    @Override
    public WbUserRegisterDO getUserRegisterByAccountId(Long accountId) {
        return wbAccountDao.getUserRegisterByAccountId(accountId);
    }

    @Override
    public WbUserOauthDO getUserOauthByAccountId(Long accountId) {
        return wbAccountDao.getUserOauthByAccountId(accountId);
    }

    @Override
    public WbUserGuideDO getUserGuideByUserId(Long userId) {
        return wbUserDao.getUserGuideByUserId(userId);
    }

    @Override
    public WbUserDisplayOrderDO getUserDisplayOrderByUserId(Long userId) {
        return wbUserDao.getUserDisplayOrderByUserId(userId);
    }

    @Override
    public WbUserUiSettingDO getUserUiSettingByUserId(Long userId) {
        return wbUserDao.getUserUiSettingByUserId(userId);
    }

    @Override
    public WbUserFunctionSettingDO getUserFunctionSettingByUserId(Long userId) {
        return wbUserDao.getUserFunctionSettingByUserId(userId);
    }

    @Override
    public void saveOrUpdateAccount(WbAccountDO accountDO){
        wbAccountDao.saveOrUpdateAccount(accountDO);
    }

    @Override
    public void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO){
        wbAccountDao.saveOrUpdateUserOauth(userOauthDO);
    }

    @Override
    public WbOuterDataUserDO getOuterDataUserByClientAndOuterId(String client, String outerId){
        return wbOuterDataUserDao.getOuterDataUserByClientAndOuterId(client, outerId);
    }

    @Override
    public WbUserDO getUserById(Long id){
        return wbUserDao.getUserById(id);
    }

    @Override
    public WbUserDO getUserByClientAndOuterId(String client, String outerCombineId) {
        return wbUserDao.getUserByClientAndOuterId(client, outerCombineId);
    }

    @Override
    public WbUserOauthDO getUserOauthByDdUnionId(String ddUnionId){
        return wbAccountDao.getUserOauthByDdUnionId(ddUnionId);
    }

    @Override
    public WbAccountDO getAccountById(Long id){
        return wbAccountDao.getAccountById(id);
    }

    /**
     * 逻辑如下：
     * 1  如果params中有unionId，那么首先根据unionId查找是否有相同unionId的Account。
     *    如果用户之前使用钉钉扫码登录过web端，那么可能会出现这种情况。
     * 2  如果没有unionId
     * @param userVO
     * @return
     */
    @Override
    public WbUserDO saveUserInfo(Long teamId, AutoCreateUserVO userVO){
        //  新增account
        //  如果unionId在数据库中存在，那么就根据unionId获取account，否则就新增account，并且保存unionId
        WbAccountDO accountDO = null;
        if(userVO.getOuterUnionId() != null){
            WbUserOauthDO userOauthDO = wbAccountDao.getUserOauthByDdUnionId(userVO.getOuterUnionId());
            if(userOauthDO != null){
                accountDO = wbAccountDao.getAccountById(userOauthDO.getAccountId());
            }
        }
        if(accountDO == null){
            //  保存account
            accountDO = UserFactory.getAccountDO();
            accountDO.setPassword(RandomStringUtils.randomAlphabetic(6));
            accountDO.setName(userVO.getName());
            accountDO.setAvatar(userVO.getAvatar());
            wbAccountDao.saveOrUpdateAccount(accountDO);

            //  保存userRegister
            WbUserRegisterDO userRegister = UserFactory.getUserRegisterDO();
            userRegister.setClient(userVO.getClient());
            userRegister.setMode(userVO.getClient());
            userRegister.setRegDate(new Date());
            userRegister.setAccountId(accountDO.getId());
            wbAccountDao.saveOrUpdateUserRegister(userRegister);

            //  保存userOauth
            if(userVO.getOuterUnionId() != null){
                WbUserOauthDO userOauthDO = UserFactory.getUserOauthDO();
                userOauthDO.setAccountId(accountDO.getId());
                userOauthDO.setDdUnionId(userVO.getOuterUnionId());
                wbAccountDao.saveOrUpdateUserOauth(userOauthDO);
            }
        }
        //  新增user
        WbUserDO userDO = UserFactory.getUserDO();
        userDO.setAccountId(accountDO.getId());
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
     * @param userDO
     * @param userVO
     * @return
     */
    @Override
    public WbUserDO updateUserInfo(WbUserDO userDO, AutoCreateUserVO userVO) {
        WbUserDO updatedUserDO = null;
        WbAccountDO updatedAccountDO = null;
        WbUserOauthDO updatedUserOauthDO = null;

        if(userVO.getName() != null){
            updatedUserDO = userDO;
            updatedUserDO.setName(userVO.getName());
            updatedAccountDO = wbAccountDao.getAccountById(userDO.getAccountId());
            updatedAccountDO.setName(userVO.getName());
        }
        if(userVO.getAvatar() != null){
            updatedAccountDO = updatedAccountDO == null ? wbAccountDao.getAccountById(userDO.getAccountId()) : updatedAccountDO;
            updatedAccountDO.setAvatar(userVO.getAvatar());
        }
//        if(userVO.getOuterUnionId() != null){
//            updatedUserOauthDO = wbAccountDao.getUserOauthByDdUnionId(userVO.getOuterUnionId());
//            if(updatedUserOauthDO == null){
//                updatedUserOauthDO
//            }
//            updatedUserOauthDO.setDdUnionId(userVO.getOuterUnionId());
//        }
        //  执行更新
        if(updatedUserDO != null){
            wbUserDao.saveOrUpdateUser(updatedUserDO);
        }
        if(updatedAccountDO != null){
            wbAccountDao.saveOrUpdateAccount(updatedAccountDO);
        }
//        if(updatedUserOauthDO != null){
//            wbAccountDao.saveOrUpdateUserOauth(updatedUserOauthDO);
//        }

        return userDO;
    }
}
