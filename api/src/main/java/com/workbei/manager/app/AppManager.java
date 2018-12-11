package com.workbei.manager.app;

import com.workbei.model.domain.user.WbOuterDataAppDO;

/**
 * @author Wallace Mao
 * Date: 2018-12-11 14:18
 */
public interface AppManager {
    void saveOrUpdateOuterDataApp(WbOuterDataAppDO wbOuterDataAppDO);

    WbOuterDataAppDO getOuterDataAppByToken(String token);
}
