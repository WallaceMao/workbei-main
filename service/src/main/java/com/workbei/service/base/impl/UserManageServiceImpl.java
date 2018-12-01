package com.workbei.service.base.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.*;
import com.workbei.model.domain.user.*;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.base.UserManageService;
import com.workbei.util.LogFormatter;
import factory.UserFactory;
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
    @Autowired
    private WbOuterDataTeamDao wbOuterDataTeamDao;
    @Autowired
    private WbRoleDao wbRoleDao;

    @Override
    public WbOuterDataUserDO getOuterDataUserByClientAndOuterId(String client, String outerId){
        return wbOuterDataUserDao.getOuterDataUserByClientAndOuterId(client, outerId);
    }

    @Override
    public WbUserDO getUserById(Long id){
        return wbUserDao.getUserById(id);
    }

    @Override
    public WbUserOauthDO getUserOauthByDdUnionId(String ddUnionId){
        return wbAccountDao.getUserOauthByDdUnionId(ddUnionId);
    }

    @Override
    public WbAccountDO getAccountById(Long id){
        return wbAccountDao.getAccountById(id);
    }

    @Override
    public WbRoleGroupDO getCommonRoleGroup(){
        return wbRoleDao.getRoleGroupByName(WbConstant.APP_ROLE_GROUP_USER);
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
    public WbUserDO saveUserInfo(AutoCreateUserVO userVO, WbTeamDO teamDO, WbRoleGroupDO roleGroupDO){
        //  如果outerDataUser中存在，那么就不创建，直接返回
        WbOuterDataUserDO outerDataUserDO = wbOuterDataUserDao.getOuterDataUserByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if(outerDataUserDO != null){
            return wbUserDao.getUserById(outerDataUserDO.getUserId());
        }
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
        if(teamDO != null){
            userDO.setTeamId(teamDO.getId());
        }
        if(userVO.getOuterCorpId() != null){
            WbOuterDataTeamDO outerDataTeamDO = wbOuterDataTeamDao.getOuterDataTeamByClientAndOuterId(
                    userVO.getClient(),
                    userVO.getOuterCorpId()
            );
            if(outerDataTeamDO != null){
                userDO.setTeamId(outerDataTeamDO.getTeamId());
            }else{
                logger.warn(LogFormatter.format(
                        LogFormatter.LogEvent.COMMON,
                        "can not get outerDataTeamDO by corpId",
                        new LogFormatter.KeyValue("autoCreateUserVO: ", userVO)
                ));
            }
        }
        wbUserDao.saveOrUpdateUser(userDO);
        Long userId = userDO.getId();
        if(userVO.getOuterCombineId() != null){
            outerDataUserDO = UserFactory.getOuterDataUserDO();
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
        WbUserRoleGroupDO userRoleGroupDO = UserFactory.getUserRoleGroupDO();
        userRoleGroupDO.setUserId(userId);
        userRoleGroupDO.setRoleGroupId(roleGroupDO.getId());
        wbUserDao.saveOrUpdateUserRoleGroup(userRoleGroupDO);
        WbUserUiSettingDO userUiSettingDO = UserFactory.getUserUiSettingDO();
        userUiSettingDO.setUserId(userId);
        wbUserDao.saveOrUpdateUserUiSetting(userUiSettingDO);
        WbUserFunctionSettingDO userFunctionSettingDO = UserFactory.getUserFunctionSettingDO();
        userFunctionSettingDO.setUserId(userId);
        wbUserDao.saveOrUpdateUserFunctionSetting(userFunctionSettingDO);

        //TODO 生成默认的标签，暂时不做

        return userDO;
    }

    @Override
    public WbUserDO updateUserInfo(AutoCreateUserVO userVO, WbUserDO userDO) {
        if(userVO.getName() != null){
            userDO.setName(userVO.getName());
            wbUserDao.saveOrUpdateUser(userDO);
        }
        if(userVO.getAvatar() != null){
            WbAccountDO accountDO = wbAccountDao.getAccountById(userDO.getAccountId());
            accountDO.setAvatar(userVO.getAvatar());
            wbAccountDao.saveOrUpdateAccount(accountDO);
        }
        if(userVO.getOuterUnionId() != null){
            WbUserOauthDO userOauthDO = wbAccountDao.getUserOauthByDdUnionId(userVO.getOuterUnionId());
            userOauthDO.setDdUnionId(userVO.getOuterUnionId());
            wbAccountDao.saveOrUpdateUserOauth(userOauthDO);
        }
        return userDO;
    }

    @Override
    public void saveOrUpdateUser(WbUserDO userDO) {
        wbUserDao.saveOrUpdateUser(userDO);
    }

    @Override
    public void saveOrUpdateAccount(WbAccountDO accountDO){
        wbAccountDao.saveOrUpdateAccount(accountDO);
    }

    @Override
    public void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO){
        wbAccountDao.saveOrUpdateUserOauth(userOauthDO);
    }
}
