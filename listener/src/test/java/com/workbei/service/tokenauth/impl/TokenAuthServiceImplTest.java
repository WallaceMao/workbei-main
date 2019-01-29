package com.workbei.service.tokenauth.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.manager.app.AppManager;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.service.tokenauth.TokenAuthService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class TokenAuthServiceImplTest extends BaseUnitTest {
    @Autowired
    private AppManager appManager;
    @Autowired
    private TokenAuthService tokenAuthService;

    @Test
    public void checkToken() throws Exception {
        Date now = new Date();
        String randomToken  ="random_token_" + now.getTime();
        //  token不存在，返回false
        assertThat(tokenAuthService.checkToken(randomToken)).isFalse();

        WbOuterDataAppDO appDO = new WbOuterDataAppDO();
        appDO.setName("appName_" + now.getTime());
        appDO.setKey("random_key_" + now.getTime());
        appDO.setToken(randomToken);
        appDO.setWhiteIpList("127.0.0.1");
        appManager.saveOrUpdateOuterDataApp(appDO);
        // token不是WbConstant.APP_DEFAULT_CLIENT，返回false
        assertThat(tokenAuthService.checkToken(appDO.getToken())).isFalse();

        WbOuterDataAppDO appDO2 = new WbOuterDataAppDO();
        appDO2.setName("appName_" + now.getTime());
        appDO2.setKey(WbConstant.APP_DEFAULT_CLIENT);
        appDO2.setToken("appToken_" + now.getTime());
        appDO2.setWhiteIpList("127.0.0.1");
        appManager.saveOrUpdateOuterDataApp(appDO2);
        assertThat(tokenAuthService.checkToken(appDO2.getToken())).isTrue();
    }

    @Test
    public void getAppWhiteIpList() {
        Date now = new Date();
        String randomToken  ="random_token_" + now.getTime();
        assertThat(tokenAuthService.getAppWhiteIpList(randomToken)).isNull();

        WbOuterDataAppDO appDO = new WbOuterDataAppDO();
        appDO.setName("appName_" + now.getTime());
        appDO.setKey("random_key_" + now.getTime());
        appDO.setToken(randomToken);
        appDO.setWhiteIpList("127.0.0.1");
        appManager.saveOrUpdateOuterDataApp(appDO);
        assertThat(tokenAuthService.getAppWhiteIpList(appDO.getToken())).isEqualTo(appDO.getWhiteIpList());
    }
}