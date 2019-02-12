package com.workbei.manager.user.impl;

import com.workbei.constant.WbConstant;
import com.workbei.dao.user.WbAccountDao;
import com.workbei.exception.ExceptionCode;
import com.workbei.exception.WorkbeiServiceException;
import com.workbei.factory.UserFactory;
import com.workbei.manager.user.AccountManager;
import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbUserOauthDO;
import com.workbei.model.domain.user.WbUserRegisterDO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.util.StringUtil;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

import static com.workbei.exception.ExceptionCode.ACCOUNT_NOT_FOUND;

/**
 * @author Wallace Mao
 * Date: 2018-12-05 12:12
 */
public class AccountManagerImpl implements AccountManager {
    private WbAccountDao wbAccountDao;

    public AccountManagerImpl(WbAccountDao wbAccountDao) {
        this.wbAccountDao = wbAccountDao;
    }

    //  --------account-------
    @Override
    public void saveOrUpdateAccount(WbAccountDO accountDO) {
        wbAccountDao.saveOrUpdateAccount(accountDO);
    }

    @Override
    public WbAccountDO getAccountById(Long id) {
        return wbAccountDao.getAccountById(id);
    }

    @Override
    public WbAccountDO getAccountByClientAndOuterId(String client, String outerId) {
        return wbAccountDao.getAccountByClientAndOuterId(client, outerId);
    }

    @Override
    public WbAccountDO getAccountByDdUnionId(String ddUnionId) {
        return wbAccountDao.getAccountByDdUnionId(ddUnionId);
    }

    //  --------userRegister--------
    @Override
    public WbUserRegisterDO getUserRegisterByAccountId(Long accountId) {
        return wbAccountDao.getUserRegisterByAccountId(accountId);
    }

    //  --------userOauth--------
    @Override
    public void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO) {
        wbAccountDao.saveOrUpdateUserOauth(userOauthDO);
    }

    @Override
    public WbUserOauthDO getUserOauthByAccountId(Long accountId) {
        return wbAccountDao.getUserOauthByAccountId(accountId);
    }

    @Override
    public WbUserOauthDO getUserOauthByDdUnionId(String ddUnionId) {
        return wbAccountDao.getUserOauthByDdUnionId(ddUnionId);
    }

    //  --------
    @Override
    public WbAccountDO saveAccountInfo(AutoCreateUserVO userVO) {
        //  保存account
        WbAccountDO accountDO = UserFactory.getAccountDO();
        accountDO.setPassword(RandomStringUtils.randomAlphabetic(6));
        accountDO.setName(userVO.getName());
        if (StringUtil.isEmpty(userVO.getAvatar())) {
            userVO.setAvatar(WbConstant.USER_DEFAULT_USER_AVATAR);
        }
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
        if (userVO.getOuterUnionId() != null) {
            WbUserOauthDO userOauthDO = UserFactory.getUserOauthDO();
            userOauthDO.setAccountId(accountDO.getId());
            userOauthDO.setDdUnionId(userVO.getOuterUnionId());
            wbAccountDao.saveOrUpdateUserOauth(userOauthDO);
        }
        return accountDO;
    }

    /**
     * 根据userVO更新accountInfo
     * @param userVO
     * @return
     */
    @Override
    public WbAccountDO updateAccountInfo(AutoCreateUserVO userVO) {
        WbAccountDO accountDO = getAccountByClientAndOuterId(
                userVO.getClient(), userVO.getOuterCombineId()
        );
        if (accountDO == null) {
            throw new WorkbeiServiceException(
                    ExceptionCode.getMessage(ACCOUNT_NOT_FOUND, userVO));
        }
        WbAccountDO updatedAccountDO = null;
        WbUserOauthDO updatedUserOauthDO = null;

        if (!StringUtil.isEmpty(userVO.getName())) {
            updatedAccountDO = updatedAccountDO == null
                    ? accountDO
                    : updatedAccountDO;
            updatedAccountDO.setName(userVO.getName());
        }
        if (!StringUtil.isEmpty(userVO.getAvatar())) {
            updatedAccountDO = updatedAccountDO == null
                    ? accountDO
                    : updatedAccountDO;
            updatedAccountDO.setAvatar(userVO.getAvatar());
        }
        if (!StringUtil.isEmpty(userVO.getOuterUnionId())) {
            updatedUserOauthDO = updatedUserOauthDO == null
                    ? getUserOauthByAccountId(accountDO.getId())
                    : updatedUserOauthDO;
            // fix NPE
            if (updatedUserOauthDO != null) {
                updatedUserOauthDO.setDdUnionId(userVO.getOuterUnionId());
            }
        }
        if (updatedAccountDO != null) {
            saveOrUpdateAccount(updatedAccountDO);
        }
        if (updatedUserOauthDO != null) {
            saveOrUpdateUserOauth(updatedUserOauthDO);
        }
        return updatedAccountDO;
    }
}
