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
    WbAccountDO getAccountById(@Param("id") Long id);
    WbUserOauthDO getUserOauthByDdUnionId(@Param("ddUnionId") String ddUnionId);
    void saveOrUpdateAccount(WbAccountDO account);
    void saveOrUpdateUserOauth(WbUserOauthDO userOauthDO);
    void saveOrUpdateUserRegister(WbUserRegisterDO userRegisterDO);
}
