package com.workbei.controller.autocreate;

import com.workbei.constant.WbConstant;
import com.workbei.controller.util.ResponseResult;
import com.workbei.controller.util.ValidateChecker;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.AutoCreateService;
import com.workbei.util.LogFormatter;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.workbei.util.LogFormatter.LogEvent;
import static com.workbei.util.LogFormatter.getKV;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 17:58
 */
@Api(
        value = "同步组织架构",
        description = "同步组织架构相关操作",
        position = 0,
        produces = "application/json",
        consumes = "application/json",
        tags = "tokenAuthV2")
@RestController("autoCreateV2Controller")
@RequestMapping("/v3w/tokenAuth")
public class AutoCreateV2Controller {
    private static final Logger bizLogger = LoggerFactory.getLogger(AutoCreateV2Controller.class);

    private ValidateChecker checker;
    private AutoCreateService autoCreateService;
    //  其他参数
    private Validator createTeamValidator;
    private Validator createDepartmentValidator;
    private Validator updateDepartmentValidator;
    private Validator createUserValidator;
    private Validator updateUserValidator;

    @Autowired
    public AutoCreateV2Controller(ValidateChecker checker, AutoCreateService autoCreateService) {
        this.checker = checker;
        this.autoCreateService = autoCreateService;
    }

    @Autowired
    public void setAutoCreateTeamValidator(Validator createTeamValidator) {
        this.createTeamValidator = createTeamValidator;
    }

    @Autowired
    public void setCreateDepartmentValidator(Validator createDepartmentValidator) {
        this.createDepartmentValidator = createDepartmentValidator;
    }

    @Autowired
    public void setUpdateDepartmentValidator(Validator updateDepartmentValidator) {
        this.updateDepartmentValidator = updateDepartmentValidator;
    }

    @Autowired
    public void setCreateUserValidator(Validator createUserValidator) {
        this.createUserValidator = createUserValidator;
    }

    @Autowired
    public void setUpdateUserValidator(Validator updateUserValidator) {
        this.updateUserValidator = updateUserValidator;
    }

    @ApiOperation(value = "创建公司", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "body", name = "name", dataType = "string",
                    value = "创建的团队名称", defaultValue = "dingtalk"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCorpId", dataType = "string",
                    value = "团队的corpId", defaultValue = "dingxyzabc"),
            @ApiImplicitParam(paramType = "body", name = "client", dataType = "string",
                    value = "客户端标识", defaultValue = "dingtalk")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PostMapping("/client/{client}/team")
    public AutoCreateTeamVO createTeam(
            @PathVariable("client") String client,
            @RequestBody AutoCreateTeamVO autoCreateTeamVO,
            HttpServletRequest request,
            HttpServletResponse response,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createTeam",
                getKV("autoCreateTeamVO", autoCreateTeamVO)
        ));
        if (autoCreateTeamVO.getClient() == null) {
            autoCreateTeamVO.setClient(makeClient(client));
        }
        if (!checkClient(request, response, autoCreateTeamVO.getClient())) {
            return null;
        }
        checker.check(autoCreateTeamVO, createTeamValidator, result);
        autoCreateService.createTeam(autoCreateTeamVO);
        return autoCreateTeamVO;
    }

    @ApiOperation(value = "创建部门", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "body", name = "name", dataType = "string",
                    value = "创建的部门名称", defaultValue = "技术部"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCorpId", dataType = "string",
                    value = "部门所属团队的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCombineId", dataType = "string",
                    value = "部门的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerParentCombineId", dataType = "string",
                    value = "父级部门的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "displayOrder", dataType = "long",
                    value = "部门的显示顺序", defaultValue = "123"),
            @ApiImplicitParam(paramType = "body", name = "client", dataType = "string",
                    value = "客户端标识", defaultValue = "dingtalk")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PostMapping("/client/{client}/department")
    public AutoCreateDepartmentVO createDepartment(
            @PathVariable("client") String client,
            @RequestBody AutoCreateDepartmentVO autoCreateDepartmentVO,
            HttpServletRequest request,
            HttpServletResponse response,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createDepartment",
                getKV("autoCreateDepartmentVO", autoCreateDepartmentVO)
        ));
        if (autoCreateDepartmentVO.getClient() == null) {
            autoCreateDepartmentVO.setClient(makeClient(client));
        }
        if (!checkClient(request, response, autoCreateDepartmentVO.getClient())) {
            return null;
        }
        checker.check(autoCreateDepartmentVO, createDepartmentValidator, result);
        autoCreateService.createDepartment(autoCreateDepartmentVO);
        return autoCreateDepartmentVO;
    }

    @ApiOperation(value = "修改部门", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "path", name = "outerCombineId", dataType = "string",
                    value = "部门的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCorpId", dataType = "string",
                    value = "部门所属团队的outerId"),
            @ApiImplicitParam(paramType = "body", name = "name", dataType = "string",
                    value = "创建的部门名称", defaultValue = "技术部"),
            @ApiImplicitParam(paramType = "body", name = "outerParentCombineId", dataType = "string",
                    value = "父级部门的outerId"),
            @ApiImplicitParam(paramType = "body", name = "displayOrder", dataType = "long",
                    value = "部门的显示顺序", defaultValue = "123"),
            @ApiImplicitParam(paramType = "body", name = "client", dataType = "string",
                    value = "客户端标识", defaultValue = "dingtalk")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PutMapping("/client/{client}/department/{outerId}")
    public Map updateDepartment(
            @PathVariable("client") String client,
            @PathVariable("outerId") String outerId,
            @RequestBody AutoCreateDepartmentVO autoCreateDepartmentVO,
            HttpServletRequest request,
            HttpServletResponse response,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateDepartment",
                getKV("outerId", outerId),
                getKV("autoCreateDepartmentVO", autoCreateDepartmentVO)
        ));
        if (autoCreateDepartmentVO.getClient() == null) {
            autoCreateDepartmentVO.setClient(makeClient(client));
        }
        if (!checkClient(request, response, autoCreateDepartmentVO.getClient())) {
            return null;
        }
        if (autoCreateDepartmentVO.getOuterCombineId() == null) {
            autoCreateDepartmentVO.setOuterCombineId(outerId);
        }
        checker.check(autoCreateDepartmentVO, updateDepartmentValidator, result);
        autoCreateService.updateDepartment(autoCreateDepartmentVO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "删除部门", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "path", name = "outerCombineId", dataType = "string",
                    value = "部门的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCorpId", dataType = "string",
                    value = "部门所属团队的outerId")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @DeleteMapping("/client/{client}/department/{outerId}")
    public Map deleteDepartment(
            @PathVariable("client") String client,
            @PathVariable("outerId") String outerId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "deleteDepartment",
                getKV("outerId", outerId)
        ));
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setClient(makeClient(client));
        autoCreateDepartmentVO.setOuterCombineId(outerId);
        if (!checkClient(request, response, autoCreateDepartmentVO.getClient())) {
            return null;
        }
        autoCreateService.deleteDepartment(autoCreateDepartmentVO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "新建用户", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCombineId", dataType = "string",
                    value = "用户的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCorpId", dataType = "string",
                    value = "用户所属团队的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCombineDeptIdList", dataType = "string",
                    value = "用户所属部门的outerId列表"),
            @ApiImplicitParam(required = true, paramType = "body", name = "name", dataType = "string",
                    value = "用户的姓名"),
            @ApiImplicitParam(paramType = "body", name = "avatar", dataType = "string",
                    value = "用户的头像"),
            @ApiImplicitParam(paramType = "body", name = "outerUnionId", dataType = "string",
                    value = "用户的unionId")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PostMapping("/client/{client}/user")
    public AutoCreateUserVO createUser(
            @PathVariable("client") String client,
            @RequestBody AutoCreateUserVO userVO,
            HttpServletRequest request,
            HttpServletResponse response,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createUser",
                getKV("userVO", userVO)
        ));
        if (userVO.getClient() == null) {
            userVO.setClient(makeClient(client));
        }
        if (!checkClient(request, response, userVO.getClient())) {
            return null;
        }
        checker.check(userVO, createUserValidator, result);
        autoCreateService.createUser(userVO);
        return userVO;
    }

    @ApiOperation(value = "更新用户", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "path", name = "outerCombineId", dataType = "string",
                    value = "用户的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "outerCorpId", dataType = "string",
                    value = "用户所属团队的outerId"),
            @ApiImplicitParam(paramType = "body", name = "outerCombineDeptIdList", dataType = "string",
                    value = "用户所属部门的outerId列表"),
            @ApiImplicitParam(paramType = "body", name = "name", dataType = "string",
                    value = "用户的姓名"),
            @ApiImplicitParam(paramType = "body", name = "avatar", dataType = "string",
                    value = "用户的头像"),
            @ApiImplicitParam(paramType = "body", name = "outerUnionId", dataType = "string",
                    value = "用户的unionId")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PutMapping("/client/{client}/user/{outerId}")
    public AutoCreateUserVO updateUser(
            @PathVariable("client") String client,
            @PathVariable("outerId") String outerId,
            @RequestBody AutoCreateUserVO userVO,
            HttpServletRequest request,
            HttpServletResponse response,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateUser",
                getKV("outerId", outerId),
                getKV("userVO", userVO)
        ));
        if (userVO.getClient() == null) {
            userVO.setClient(makeClient(client));
        }
        if (!checkClient(request, response, userVO.getClient())) {
            return null;
        }
        if (userVO.getOuterCombineId() == null) {
            userVO.setOuterCombineId(outerId);
        }
        checker.check(userVO, updateUserValidator, result);
        autoCreateService.updateUser(userVO);
        return userVO;
    }

    @ApiOperation(value = "更新的管理员状态", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "path", name = "outerCombineId", dataType = "string",
                    value = "用户的outerId"),
            @ApiImplicitParam(required = true, paramType = "path", name = "admin", dataType = "boolean",
                    value = "是否是管理员")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PutMapping("/client/{client}/user/{outerId}/admin/{admin}")
    public Map updateUserSetAdmin(
            @PathVariable("client") String client,
            @PathVariable("outerId") String outerId,
            @PathVariable("admin") Boolean isAdmin,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateUserSetAdmin",
                getKV("outerId", outerId),
                getKV("admin", isAdmin)
        ));
        AutoCreateUserVO userVO = new AutoCreateUserVO();
        userVO.setClient(makeClient(client));
        userVO.setOuterCombineId(outerId);
        userVO.setAdmin(isAdmin);
        if (!checkClient(request, response, userVO.getClient())) {
            return null;
        }
        autoCreateService.updateUserSetAdmin(userVO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "更新团队的所有管理员", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "path", name = "outerId", dataType = "string",
                    value = "团队的outerId"),
            @ApiImplicitParam(required = true, paramType = "body", name = "adminList", dataType = "list",
                    value = "管理员列表")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PutMapping("/client/{client}/team/{outerId}/admin")
    public Map updateTeamAllAdmin(
            @PathVariable("client") String client,
            @PathVariable("outerId") String outerId,
            @RequestBody List<String> adminList,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateTeamAllAdmin",
                getKV("outerId", outerId),
                getKV("adminList", adminList)
        ));
        if (!checkClient(request, response, client)) {
            return null;
        }
        autoCreateService.updateBatchUserSetAdmin(client, outerId, adminList);
        return ResponseResult.success();
    }

    @ApiOperation(value = "更新的用户，将用于移除团队", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
            @ApiImplicitParam(required = true, paramType = "path", name = "outerCombineId", dataType = "string",
                    value = "用户的outerId")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PutMapping("/client/{client}/user/{outerId}/team/null")
    public Map updateUserLeaveTeam(
            @PathVariable("client") String client,
            @PathVariable("outerId") String outerId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateUserLeaveTeam",
                getKV("outerId", outerId)
        ));
        AutoCreateUserVO userVO = new AutoCreateUserVO();
        userVO.setClient(makeClient(client));
        userVO.setOuterCombineId(outerId);
        if (!checkClient(request, response, userVO.getClient())) {
            return null;
        }
        autoCreateService.updateUserLeaveTeam(userVO);
        return ResponseResult.success();
    }

    private String makeClient(String client) {
        return client == null ? WbConstant.APP_DEFAULT_CLIENT : client;
    }

    private Boolean checkClient(HttpServletRequest request, HttpServletResponse response, String client) {
        String outerClient = (String)request.getAttribute("outerClient");
        if (client == null) {
            return false;
        }
        boolean isValid = client.equals(outerClient);
        if (!isValid) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return isValid;
    }
}
