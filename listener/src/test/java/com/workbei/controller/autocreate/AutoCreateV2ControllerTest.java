package com.workbei.controller.autocreate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.workbei.WebBaseTest;
import com.workbei.constant.WbConstant;
import com.workbei.http.HttpResultCode;
import com.workbei.manager.app.AppManager;
import com.workbei.manager.user.TeamManager;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.model.domain.user.WbTeamUserRoleDO;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.AutoCreateService;
import com.workbei.util.RegExpUtil;
import com.workbei.util.TestDepartmentFactory;
import com.workbei.util.TestTeamFactory;
import com.workbei.util.TestUserFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static com.workbei.constant.TestV2Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional(transactionManager = "transactionManager")
@Rollback
public class AutoCreateV2ControllerTest extends WebBaseTest {

    @Autowired
    private AutoCreateService autoCreateService;
    @Autowired
    private AppManager appManager;
    @Autowired
    private TeamManager teamManager;

    private WbOuterDataAppDO globalApp;
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
        appDO.setKey("abcdefg");
        appDO.setToken("appToken_" + now.getTime());
        appDO.setWhiteIpList("127.0.0.1");
        appManager.saveOrUpdateOuterDataApp(appDO);
        globalApp = appDO;

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
    }

    @Test
    public void createTeamWithError() throws Exception {

        JSONObject json = new JSONObject();
        checkSaveValidator(globalUrlCreateTeam, json.toString(), HttpResultCode.TEAM_OUTER_CORP_ID_NULL);

        json.put("outerCorpId", "dingaaaaaxxxx");
        checkSaveValidator(globalUrlCreateTeam, json.toString(), HttpResultCode.TEAM_NAME_NULL);
    }

    @Test
    public void createTeam() throws Exception {
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("dingxxxxaaaa" + new Date().getTime());
        String str = JSON.toJSONString(teamVO);

        mockMvc.perform(post(globalUrlCreateTeam)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void createDepartmentWithError() throws Exception {

        JSONObject json = new JSONObject();
        checkSaveValidator(globalUrlCreateDepartment, json.toString(), HttpResultCode.DEPT_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkSaveValidator(globalUrlCreateDepartment, json.toString(), HttpResultCode.DEPT_OUTER_ID_NULL);
        json.put("outerCombineId", "ding_outerCombineId");
        checkSaveValidator(globalUrlCreateDepartment, json.toString(), HttpResultCode.DEPT_OUTER_PARENT_ID_NULL);
        json.put("outerParentCombineId", "ding_outerParentCombineId");
        checkSaveValidator(globalUrlCreateDepartment, json.toString(), HttpResultCode.DEPT_NAME_NULL);
        json.put("name", "ding_name");
        checkSaveValidator(globalUrlCreateDepartment, json.toString(), HttpResultCode.DEPT_DISPLAY_ORDER_NULL);
        json.put("displayOrder", "not a number");
        checkSaveValidator(globalUrlCreateDepartment, json.toString(), HttpResultCode.SYSTEM_ERROR);
    }

    @Test
    public void createDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);

        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        departmentVO.setClient(globalApp.getKey());
        String str = JSON.toJSONString(departmentVO);

        mockMvc.perform(post(globalUrlCreateDepartment)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void updateDepartmentWithError() throws Exception {
        String outerId = "ding_outer_department_id";
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", outerId);
        String updateDepartmentPath = RegExpUtil.replacePathVariable(globalUrlUpdateDepartment, pathParams);
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(put(updateDepartmentPath))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        checkUpdateValidator(updateDepartmentPath, json.toString(), HttpResultCode.DEPT_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkUpdateValidator(updateDepartmentPath, json.toString(), HttpResultCode.DEPT_UPDATE_NO_FIELD);
    }

    @Test
    public void updateDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        teamVO.setCreator(userVO);
        autoCreateService.createTeam(teamVO);
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        departmentVO.setClient(globalApp.getKey());
        autoCreateService.createDepartment(departmentVO);
        departmentVO.setName("at_new_department_name" + now.getTime());
        departmentVO.setDisplayOrder((double) now.getTime());

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", departmentVO.getOuterCombineId());
        String updateDepartmentPath = RegExpUtil.replacePathVariable(globalUrlUpdateDepartment, pathParams);
        String str = JSON.toJSONString(departmentVO);

        mockMvc.perform(put(updateDepartmentPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        AutoCreateDepartmentVO departmentVO = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        departmentVO.setClient(globalApp.getKey());
        autoCreateService.createDepartment(departmentVO);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", departmentVO.getOuterCombineId());
        String deleteDepartmentPath = RegExpUtil.replacePathVariable(globalUrlDeleteDepartment, pathParams);

        mockMvc.perform(delete(deleteDepartmentPath)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testCreateUserWithError() throws Exception {
        JSONObject json = new JSONObject();
        checkSaveValidator(globalUrlCreateUser, json.toString(), HttpResultCode.USER_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkSaveValidator(globalUrlCreateUser, json.toString(), HttpResultCode.USER_OUTER_ID_NULL);
        json.put("outerCombineId", "ding_outerCombineId");
        JSONArray deptList = new JSONArray();
        json.put("outerCombineDeptIdList", null);
        checkSaveValidator(globalUrlCreateUser, json.toString(), HttpResultCode.USER_OUTER_DEPT_ID_NULL);
        deptList.add("ding_outerCombineDeptIdList");
        json.put("outerCombineDeptIdList", deptList);
        checkSaveValidator(globalUrlCreateUser, json.toString(), HttpResultCode.USER_NAME_NULL);
    }

    @Test
    public void testCreateUser() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setClient(globalApp.getKey());
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);

        String str = JSON.toJSONString(userVO);

        mockMvc.perform(post(globalUrlCreateUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testUpdateUserWithError() throws Exception {
        String outerId = "ding_outer_user_id";
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", outerId);
        String updateUserPath = RegExpUtil.replacePathVariable(globalUrlUpdateUser, pathParams);
        //  默认无权限，返回401
        MvcResult result;
        mockMvc.perform(put(updateUserPath))
                .andExpect(status().isUnauthorized())
                .andReturn();

        JSONObject json = new JSONObject();
        checkUpdateValidator(updateUserPath, json.toString(), HttpResultCode.USER_OUTER_CORP_ID_NULL);
        json.put("outerCorpId", "dingaaaaaxxxx");
        checkUpdateValidator(updateUserPath, json.toString(), HttpResultCode.USER_UPDATE_NO_FIELD);
    }

    @Test
    public void testUpdateUser() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setClient(globalApp.getKey());
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);
        userVO.setName("new name" + new Date().getTime());

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", userVO.getOuterCombineId());
        String updateUserPath = RegExpUtil.replacePathVariable(globalUrlUpdateUser, pathParams);
        String str = JSON.toJSONString(userVO);

        mockMvc.perform(put(updateUserPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdateUserSetAdmin() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setClient(globalApp.getKey());
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", userVO.getOuterCombineId());
        pathParams.put("admin", true);
        String updateUserSetAdminPath = RegExpUtil.replacePathVariable(globalUrlUpdateUserSetAdmin, pathParams);

        mockMvc.perform(put(updateUserSetAdminPath)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdateTeamAllAdmin() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);
        String client = globalApp.getKey();
        String outerCorpId = teamVO.getOuterCorpId();
        Long teamId = teamVO.getId();

        AutoCreateDepartmentVO deptVO1 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO1.setClient(client);
        deptVO1.setTop(true);
        autoCreateService.createDepartment(deptVO1);
        AutoCreateDepartmentVO deptVO2 = TestDepartmentFactory.getAutoCreateDepartmentVO(teamVO.getOuterCorpId());
        deptVO2.setClient(client);
        deptVO2.setOuterParentCombineId(deptVO1.getOuterCombineId());
        autoCreateService.createDepartment(deptVO2);

        AutoCreateUserVO userVO1 = TestUserFactory.getAutoCreateUserVO();
        userVO1.setClient(client);
        userVO1.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add(deptVO1.getOuterCombineId());
        deptList.add(deptVO2.getOuterCombineId());
        userVO1.setOuterCombineDeptIdList(deptList);
        userVO1.setAdmin(false);
        userVO1.setClient(client);
        autoCreateService.createUser(userVO1);

        AutoCreateUserVO userVO2 = TestUserFactory.getAutoCreateUserVO();
        userVO2.setOuterCorpId(teamVO.getOuterCorpId());
        userVO2.setOuterCombineDeptIdList(deptList);
        userVO2.setAdmin(false);
        userVO2.setClient(client);
        autoCreateService.createUser(userVO2);
        List<WbTeamUserRoleDO> adminList = teamManager.listTeamUserRoleByTeamIdAndRole(teamId, WbConstant.TEAM_USER_ROLE_ADMIN);
        assertThat(adminList).hasSize(0);

        String randomUserOuterId = "auto_test_random_user_outer_id_" + now.getTime();
        String user1OuterId = userVO1.getOuterCombineId();
        String user2OuterId = userVO2.getOuterCombineId();

        List<String> userOuterIdList = new ArrayList<>();
        userOuterIdList.add(randomUserOuterId);
        userOuterIdList.add(user1OuterId);
        userOuterIdList.add(user2OuterId);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("client", client);
        pathParams.put("outerId", teamVO.getOuterCorpId());
        String updateUserSetAdminPath = RegExpUtil.replacePathVariable(globalUrlUpdateTeamAllAdmin, pathParams);
        mockMvc.perform(put(updateUserSetAdminPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(userOuterIdList))
                .header("Authorization", globalApp.getToken()))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        adminList = teamManager.listTeamUserRoleByTeamIdAndRole(teamId, WbConstant.TEAM_USER_ROLE_ADMIN);

        assertThat(adminList).hasSize(2);
        assertThat(adminList).extracting("teamId", "userId", "role")
                .contains(tuple(teamId, userVO1.getId(), WbConstant.TEAM_USER_ROLE_ADMIN),
                        tuple(teamId, userVO2.getId(), WbConstant.TEAM_USER_ROLE_ADMIN));
    }

    @Test
    public void testUpdateUserRemoveTeam() throws Exception {
        Date now = new Date();
        AutoCreateTeamVO teamVO = TestTeamFactory.getAutoCreateTeamVO();
        teamVO.setClient(globalApp.getKey());
        teamVO.setOuterCorpId("auto_test_team_outer_id_" + now.getTime());
        autoCreateService.createTeam(teamVO);

        AutoCreateUserVO userVO = TestUserFactory.getAutoCreateUserVO();
        userVO.setClient(globalApp.getKey());
        userVO.setOuterCorpId(teamVO.getOuterCorpId());
        List<String> deptList = new ArrayList<>();
        deptList.add("dept_not_exist");
        userVO.setOuterCombineDeptIdList(deptList);
        autoCreateService.createUser(userVO);

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("outerId", userVO.getOuterCombineId());
        String updateUserRemoveTeamPath = RegExpUtil.replacePathVariable(globalUrlUpdateUserRemoveTeam, pathParams);

        mockMvc.perform(put(updateUserRemoveTeamPath)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private void checkSaveValidator(String url, String content, HttpResultCode expectedCode) throws Exception {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errcode").value(expectedCode.getCode()))
                .andExpect(jsonPath("$.errmsg").value(expectedCode.getMsg()))
                .andReturn();
    }

    private void checkUpdateValidator(String url, String content, HttpResultCode expectedCode) throws Exception {
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", globalApp.getToken()))
//                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errcode").value(expectedCode.getCode()))
                .andExpect(jsonPath("$.errmsg").value(expectedCode.getMsg()))
                .andReturn();
    }
}