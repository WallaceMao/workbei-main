package com.workbei.service.biz.impl;

import com.workbei.BaseUnitTest;
import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.WbOuterDataTeamDO;
import com.workbei.service.biz.DemoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class DemoServiceImplTest extends BaseUnitTest {
    @Autowired
    private DemoService demoService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void showHello() throws Exception {
        String name = "Wallace";
        String result = demoService.showHello(name);
        assertEquals("hello, " + name, result);
    }

    @Test
    public void testSaveOuterDataTeam(){
        Long teamId = 99999L;
        String outerId = "aaabbbccc";
        demoService.saveOuterDataTeam(outerId, teamId);
        WbOuterDataTeamDO outerDataTeamDO = demoService.getOuterDataTeamByOuterId(outerId);
        assertEquals(teamId, outerDataTeamDO.getTeamId());
        assertEquals(outerId, outerDataTeamDO.getOuterId());
        assertEquals(WbConstant.APP_DEFAULT_CLIENT, outerDataTeamDO.getClient());
    }
}