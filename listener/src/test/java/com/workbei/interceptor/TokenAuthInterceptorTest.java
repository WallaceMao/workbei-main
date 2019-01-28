package com.workbei.interceptor;

import com.workbei.WebBaseTest;
import com.workbei.manager.app.AppManager;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.util.RegExpUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.workbei.constant.TestConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TokenAuthInterceptorTest extends WebBaseTest {
    @Autowired
    private AppManager appManager;

    private static final String SUB_NETWORK = "172.16.0.0/32";
    private static final String IP_1 = "172.16.0.1";
    private static final String IP_2 = "192.168.0.1";
    private WbOuterDataAppDO globalApp = null;

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
    }

    @Test
    public void testNoAuthorization() throws Exception {
        Date now = new Date();
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        checkNoAuthorization(post(URL_CREATE_TEAM));
        checkNoAuthorization(post(URL_CREATE_DEPARTMENT));
        checkNoAuthorization(post(URL_CREATE_USER));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(URL_UPDATE_DEPARTMENT, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER_SET_ADMIN, params)));
        checkNoAuthorization(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER_REMOVE_TEAM, params)));
        checkNoAuthorization(delete(RegExpUtil.replacePathVariable(URL_DELETE_DEPARTMENT, params)));
    }

    @Test
    public void testIpNotInWhiteList() throws Exception {
        Date now = new Date();
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        checkIpNotInWhiteList(post(URL_CREATE_TEAM), IP_2);
        checkIpNotInWhiteList(post(URL_CREATE_DEPARTMENT), IP_2);
        checkIpNotInWhiteList(post(URL_CREATE_USER), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(URL_UPDATE_DEPARTMENT, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER_SET_ADMIN, params)), IP_2);
        checkIpNotInWhiteList(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER_REMOVE_TEAM, params)), IP_2);
        checkIpNotInWhiteList(delete(RegExpUtil.replacePathVariable(URL_DELETE_DEPARTMENT, params)), IP_2);
    }

    @Test
    public void testCheckInvalidToken() throws Exception {
        Date now = new Date();
        Map<String, String> params = new HashMap<>();
        params.put("outerId", String.valueOf(now.getTime()));
        params.put("admin", String.valueOf(true));
        String invalidToken = "invalid_token_" + now.getTime();
        checkInvlidaToken(post(URL_CREATE_TEAM), IP_1, invalidToken);
        checkInvlidaToken(post(URL_CREATE_DEPARTMENT), IP_1, invalidToken);
        checkInvlidaToken(post(URL_CREATE_USER), IP_1, invalidToken);
        checkInvlidaToken(put(RegExpUtil.replacePathVariable(URL_UPDATE_DEPARTMENT, params)), IP_1, invalidToken);
        checkInvlidaToken(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER, params)), IP_1, invalidToken);
        checkInvlidaToken(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER_SET_ADMIN, params)), IP_1, invalidToken);
        checkInvlidaToken(put(RegExpUtil.replacePathVariable(URL_UPDATE_USER_REMOVE_TEAM, params)), IP_1, invalidToken);
        checkInvlidaToken(delete(RegExpUtil.replacePathVariable(URL_DELETE_DEPARTMENT, params)), IP_1, invalidToken);
    }

    private void checkNoAuthorization(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        //  默认无权限，返回401
        mockMvc.perform(requestBuilder)
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

    private void checkInvlidaToken(MockHttpServletRequestBuilder requestBuilder, String ip, String token) throws Exception {
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