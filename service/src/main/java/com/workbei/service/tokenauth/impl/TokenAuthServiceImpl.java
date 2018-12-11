package com.workbei.service.tokenauth.impl;

import com.workbei.constant.WbConstant;
import com.workbei.manager.app.AppManager;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.service.tokenauth.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-12-11 14:37
 */
public class TokenAuthServiceImpl implements TokenAuthService {
    private AppManager appManager;

    @Autowired
    public TokenAuthServiceImpl(AppManager appManager) {
        this.appManager = appManager;
    }

    @Override
    public Boolean checkToken(String token) {
        WbOuterDataAppDO outerDataAppDO = appManager.getOuterDataAppByToken(token);
        if (outerDataAppDO == null) {
            return false;
        }
        if (!WbConstant.APP_DEFAULT_CLIENT.equals(outerDataAppDO.getKey())) {
            return false;
        }
        return true;
    }

    @Override
    public String getAppWhiteIpList(String token) {
        WbOuterDataAppDO outerDataAppDO = appManager.getOuterDataAppByToken(token);
        if (outerDataAppDO == null) {
            return null;
        }
        return outerDataAppDO.getWhiteIpList();
    }
}
