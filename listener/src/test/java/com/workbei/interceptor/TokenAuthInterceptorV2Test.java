package com.workbei.interceptor;

import com.workbei.WebBaseTest;
import com.workbei.manager.app.AppManager;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.util.RegExpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.workbei.constant.TestV2Constant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class TokenAuthInterceptorV2Test extends WebBaseTest {
    @Autowired
    private AppManager appManager;

    private static final String SUB_NETWORK = "172.16.0.0/16";
    private static final String IP_1 = "172.16.0.1";
    private static final String IP_2 = "192.168.0.1";
    private WbOuterDataAppDO globalApp = null;
    private WbOuterDataAppDO globalApp2 = null;
    private String globalUrlCreateTeam;
    private String globalUrlCreateDepartment;
    private String globalUrlUpdateDepartment;
    private String globalUrlDeleteDepartment;
    private String globalUrlCreateUser;
    private String globalUrlUpdateUser;
    private String globalUrlUpdateUserSetAdmin;
    private String globalUrlUpdateUserRemoveTeam;
    private String globalUrlUpdateTeamAllAdmin;

    @Before
    public void setUp() throws Exception {
        Date now = new Date();
        WbOuterDataAppDO appDO = new WbOuterDataAppDO();
        appDO.setName("appName_" + now.getTime());
        appDO.setKey("appKey_" + now.getTime());
        appDO.setToken("appToken_" + now.getTime());
        appDO.setWhiteIpList(SUB_NETWORK);
        appManager.saveOrUpdateOuterDataApp(appDO);
        globalApp = appDO;

        WbOuterDataAppDO appDO2 = new WbOuterDataAppDO();
        appDO2.setName("appName2_" + now.getTime());
        appDO2.setKey("appKey2_" + now.getTime());
        appDO2.setToken("appToken2_" + now.getTime());
        appDO2.setWhiteIpList(SUB_NETWORK);
        appManager.saveOrUpdateOuterDataApp(appDO2);
        globalApp2 = appDO2;

        Map<String, String> params = new HashMap<>();
        params.put("client", globalApp.getKey());
        globalUrlCreateTeam=RegExpUtil.replacePathVariable(URL_CREATE_TEAM_V2, params);
        globalUrlCreateDepartment=RegExpUtil.replacePathVariable(URL_CREATE_DEPARTMENT_V2, params);
        globalUrlUpdateDepartment=RegExpUtil.replacePathVariable(URL_UPDATE_DEPARTMENT_V2, params);
        globalUrlDeleteDepartment=RegExpUtil.replacePathVariable(URL_DELETE_DEPARTMENT_V2, params);
        globalUrlCreateUser=RegExpUtil.replacePathVariable(URL_CREATE_USER_V2, params);
        globalUrlUpdateUser=RegExpUtil.replacePathVariable(URL_UPDATE_USER_V2, params);
        globalUrlUpdateUserSetAdmin=RegExpUtil.replacePathVariable(URL_UPDATE_USER_SET_ADMIN_V2, params);
        globalUrlUpdateUserRemoveTeam=RegExpUtil.replacePathVariable(URL_UPDATE_USER_REMOVE_TEAM_V2, params);
        globalUrlUpdateTeamAllAdmin=RegExpUtil.replacePathVariable(URL_UPDATE_TEAM_ALL_ADMIN_V2, params);
    }

    @After
    public void tearDown() throws Exception {
        appManager.deleteOuterDataAppByKey(globalApp.getKey());
        appManager.deleteOuterDataAppByKey(globalApp2.getKey());
    }

    @Test
    public void testNoAuthorization() throws Exception {
        Date now = new Date();
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        checkNoAuthorization(post(RegExpUtil.replacePathVariable(globalUrlCreateTeam, params)));
        checkNoAuthorization(post(RegExpUtil.replacePathVariable(globalUrlCreateDepartment, params)));
        checkNoAuthorization(post(RegExpUtil.replacePathVariable(globalUrlCreateUser, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(globalUrlUpdateDepartment, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(globalUrlUpdateUser, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserSetAdmin, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserRemoveTeam, params)));
        checkNoAuthorization(delete(RegExpUtil.replacePathVariable(globalUrlDeleteDepartment, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(globalUrlUpdateTeamAllAdmin, params)));
    }

    @Test
    public void testNoAuthorizationWhenClientAndTokenNotMatch() throws Exception {
        Date now = new Date();
        String emptyJsonObject = "{}";
        String emptyJsonArray = "[]";
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        checkNoAuthorizationWhenClientAndTokenNotMatch(post(RegExpUtil.replacePathVariable(globalUrlCreateTeam, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(post(RegExpUtil.replacePathVariable(globalUrlCreateDepartment, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(post(RegExpUtil.replacePathVariable(globalUrlCreateUser, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(put(RegExpUtil.replacePathVariable(globalUrlUpdateDepartment, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(put(RegExpUtil.replacePathVariable(globalUrlUpdateUser, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserSetAdmin, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserRemoveTeam, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(delete(RegExpUtil.replacePathVariable(globalUrlDeleteDepartment, params)), emptyJsonObject);
        checkNoAuthorizationWhenClientAndTokenNotMatch(put(RegExpUtil.replacePathVariable(globalUrlUpdateTeamAllAdmin, params)), emptyJsonArray);
    }

    @Test
    public void testIpNotInWhiteList() throws Exception {
        Date now = new Date();
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        checkIpNotInWhiteList(post(RegExpUtil.replacePathVariable(globalUrlCreateTeam, params)), IP_2);
        checkIpNotInWhiteList(post(RegExpUtil.replacePathVariable(globalUrlCreateDepartment, params)), IP_2);
        checkIpNotInWhiteList(post(RegExpUtil.replacePathVariable(globalUrlCreateUser, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(globalUrlUpdateDepartment, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(globalUrlUpdateUser, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserSetAdmin, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserRemoveTeam, params)), IP_2);
        checkIpNotInWhiteList(delete(RegExpUtil.replacePathVariable(globalUrlDeleteDepartment, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(globalUrlUpdateTeamAllAdmin, params)), IP_2);
    }

    @Test
    public void testCheckInvalidToken() throws Exception {
        Date now = new Date();
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        String invalidToken = "invalid_token_" + now.getTime();
        checkInvalidToken(post(RegExpUtil.replacePathVariable(globalUrlCreateTeam, params)), IP_1, invalidToken);
        checkInvalidToken(post(RegExpUtil.replacePathVariable(globalUrlCreateDepartment, params)), IP_1, invalidToken);
        checkInvalidToken(post(RegExpUtil.replacePathVariable(globalUrlCreateUser, params)), IP_1, invalidToken);
        checkInvalidToken(put(RegExpUtil.replacePathVariable(globalUrlUpdateDepartment, params)), IP_1, invalidToken);
        checkInvalidToken(put(RegExpUtil.replacePathVariable(globalUrlUpdateUser, params)), IP_1, invalidToken);
        checkInvalidToken(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserSetAdmin, params)), IP_1, invalidToken);
        checkInvalidToken(put(RegExpUtil.replacePathVariable(globalUrlUpdateUserRemoveTeam, params)), IP_1, invalidToken);
        checkInvalidToken(delete(RegExpUtil.replacePathVariable(globalUrlDeleteDepartment, params)), IP_1, invalidToken);
        checkInvalidToken(put(RegExpUtil.replacePathVariable(globalUrlUpdateTeamAllAdmin, params)), IP_1, invalidToken);
    }

    private void checkNoAuthorization(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        //  默认无权限，返回401
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private void checkNoAuthorizationWhenClientAndTokenNotMatch(MockHttpServletRequestBuilder requestBuilder, String content) throws Exception {
        //  默认无权限，返回401
        mockMvc.perform(requestBuilder
                .header("Authorization", globalApp2.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .with(remoteAddr(IP_1))
                .content(content))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private void checkIpNotInWhiteList(MockHttpServletRequestBuilder requestBuilder, String ip) throws Exception {
        mockMvc.perform(requestBuilder
                .header("Authorization", globalApp.getToken())
                .with(remoteAddr(ip))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private void checkInvalidToken(MockHttpServletRequestBuilder requestBuilder, String ip, String token) throws Exception {
        mockMvc.perform(requestBuilder
                .header("Authorization", token)
                .with(remoteAddr(ip))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private static RequestPostProcessor remoteAddr(final String addr) {
        return new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest mockHttpServletRequest) {
                mockHttpServletRequest.setRemoteAddr(addr);
                return mockHttpServletRequest;
            }
        };
    }
}