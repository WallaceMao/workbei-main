package com.workbei.manager.app.impl;

import com.workbei.dao.app.WbOuterDataAppDao;
import com.workbei.manager.app.AppManager;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-12-11 14:18
 */
public class AppManagerImpl implements AppManager {
    private WbOuterDataAppDao wbOuterDataAppDao;

    public AppManagerImpl(WbOuterDataAppDao wbOuterDataAppDao) {
        this.wbOuterDataAppDao = wbOuterDataAppDao;
    }

    @Override
    public void saveOrUpdateOuterDataApp(WbOuterDataAppDO wbOuterDataAppDO) {
        wbOuterDataAppDao.saveOrUpdate(wbOuterDataAppDO);
    }

    @Override
    public WbOuterDataAppDO getOuterDataAppByToken(String token) {
        return wbOuterDataAppDao.getOuterDataAppByToken(token);
    }

    @Override
    public WbOuterDataAppDO getOuterDataAppByKey(String key) {
        return wbOuterDataAppDao.getOuterDataAppByKey(key);
    }

    @Override
    public void deleteOuterDataAppByKey(String key) {
        wbOuterDataAppDao.deleteOuterDataAppByKey(key);
    }
}
