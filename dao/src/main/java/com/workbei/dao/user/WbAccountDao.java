package com.workbei.dao.user;

import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbUserOauthDO;
import com.workbei.model.domain.user.WbUserRegisterDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:17
 */
@Repository("wbAccountDao")
public interface WbAccountDao {
    void saveOrUpdateAccount(WbAccountDO account);
    WbAccountDO getAccountById(
            @Param("id") Long id);
    WbAccountDO getAccountByClientAndOuterId(
            @Param("client") String client,
            @Param("outerId") String outerId);
    WbAccountDO getAccountByDdUnionId(
            @Param("ddUnionId") String ddUnionId);

    void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO);
    WbUserOauthDO getUserOauthByAccountId(
            @Param("accountId") Long accountId);
    WbUserOauthDO getUserOauthByDdUnionId(
            @Param("ddUnionId") String ddUnionId);

    void saveOrUpdateUserRegister(WbUserRegisterDO userRegisterDO);
    WbUserRegisterDO getUserRegisterByAccountId(
            @Param("accountId") Long accountId);
}
