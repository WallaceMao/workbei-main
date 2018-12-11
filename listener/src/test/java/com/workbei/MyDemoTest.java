package com.workbei;

import com.workbei.dao.app.WbOuterDataAppDao;
import com.workbei.dao.user.WbUserDao;
import com.workbei.manager.user.AccountManager;
import com.workbei.manager.user.UserManager;
import com.workbei.model.domain.user.WbAccountDO;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.model.domain.user.WbUserDO;
import com.workbei.util.TestUserFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Wallace Mao
 * Date: 2018-12-05 16:00
 */
@Transactional(transactionManager = "transactionManager")
@Rollback
public class MyDemoTest extends BaseUnitTest {
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private WbUserDao wbUserDao;
    @Autowired
    private WbOuterDataAppDao wbOuterDataAppDao;

    @Test
    public void test1() throws Exception {
        Date now = new Date();
        WbOuterDataAppDO appDO = new WbOuterDataAppDO();
        appDO.setKey("app_key_" + now.getTime());
        appDO.setName("app_name_" + now.getTime());
        appDO.setToken("app_token_" + now.getTime());
        appDO.setWhiteIpList("0.0.0.0");
        wbOuterDataAppDao.saveOrUpdate(appDO);

        WbOuterDataAppDO savedApp = wbOuterDataAppDao.getById(appDO.getId());
        assertThat(savedApp).isEqualToIgnoringGivenFields(appDO, "version");

        String newName = "new_name_" + now.getTime();
        appDO.setName(newName);
        wbOuterDataAppDao.saveOrUpdate(appDO);
        savedApp = wbOuterDataAppDao.getById(appDO.getId());
        assertThat(savedApp.getName()).isEqualTo(newName);

        wbOuterDataAppDao.deleteById(appDO.getId());
        savedApp = wbOuterDataAppDao.getById(appDO.getId());
        assertThat(savedApp).isNull();
    }

    @Test
    public void test2() throws Exception {
        WbAccountDO accountDO = TestUserFactory.getAccountDO();
        accountManager.saveOrUpdateAccount(accountDO);
        WbUserDO userDO = TestUserFactory.getUserDO();
        userDO.setAccountId(accountDO.getId());
        userManager.saveOrUpdateUser(userDO);

        userDO = wbUserDao.getUserWithAccountById(userDO.getId());
        assertThat(userDO.getAccount()).isNotNull();
    }
}
