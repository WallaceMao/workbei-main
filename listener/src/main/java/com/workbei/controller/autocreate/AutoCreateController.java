package com.workbei.controller.autocreate;

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

import java.util.Map;

import static com.workbei.util.LogFormatter.*;

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
        tags = "tokenAuth")
@RestController("autoCreateController")
@RequestMapping("/v3w/tokenAuth")
public class AutoCreateController {
    private static final Logger bizLogger = LoggerFactory.getLogger(AutoCreateController.class);

    private ValidateChecker checker;
    private AutoCreateService autoCreateService;
    //  其他参数
    private Validator createTeamValidator;
    private Validator createDepartmentValidator;
    private Validator updateDepartmentValidator;
    private Validator createUserValidator;
    private Validator updateUserValidator;

    @Autowired
    public AutoCreateController(ValidateChecker checker, AutoCreateService autoCreateService) {
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
    @PostMapping("/team")
    public AutoCreateTeamVO createTeam(
            @RequestBody AutoCreateTeamVO autoCreateTeamVO,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createTeam",
                getKV("autoCreateTeamVO", autoCreateTeamVO)
        ));
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
    @PostMapping("/department")
    public AutoCreateDepartmentVO createDepartment(
            @RequestBody AutoCreateDepartmentVO autoCreateDepartmentVO,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createDepartment",
                getKV("autoCreateDepartmentVO", autoCreateDepartmentVO)
        ));
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
    @PutMapping("/department/{outerId}")
    public Map updateDepartment(
            @PathVariable("outerId") String outerId,
            @RequestBody AutoCreateDepartmentVO autoCreateDepartmentVO,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateDepartment",
                getKV("outerId", outerId),
                getKV("autoCreateDepartmentVO", autoCreateDepartmentVO)
        ));
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
    @DeleteMapping("/department/{outerId}")
    public Map deleteDepartment(
            @PathVariable("outerId") String outerId
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "deleteDepartment",
                getKV("outerId", outerId)
        ));
        AutoCreateDepartmentVO autoCreateDepartmentVO = new AutoCreateDepartmentVO();
        autoCreateDepartmentVO.setOuterCombineId(outerId);
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
    @PostMapping("/user")
    public AutoCreateUserVO createUser(
            @RequestBody AutoCreateUserVO userVO,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createUser",
                getKV("userVO", userVO)
        ));
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
    @PutMapping("/user/{outerId}")
    public AutoCreateUserVO updateUser(
            @PathVariable("outerId") String outerId,
            @RequestBody AutoCreateUserVO userVO,
            BindingResult result
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateUser",
                getKV("outerId", outerId),
                getKV("userVO", userVO)
        ));
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
            @ApiImplicitParam(required = true, paramType = "path", name = "isAdmin", dataType = "boolean",
                    value = "是否是管理员")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "参数错误，未提供正确的参数"),
            @ApiResponse(code = 401, message = "无权限访问，比如token未提供"),
            @ApiResponse(code = 500, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
    })
    @PutMapping("/user/{outerId}/admin/{isAdmin}")
    public Map updateUserSetAdmin(
            @PathVariable("outerId") String outerId,
            @PathVariable("isAdmin") Boolean isAdmin
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateUserSetAdmin",
                getKV("outerId", outerId),
                getKV("isAdmin", isAdmin)
        ));
        AutoCreateUserVO userVO = new AutoCreateUserVO();
        userVO.setOuterCombineId(outerId);
        userVO.setAdmin(isAdmin);
        autoCreateService.updateUserSetAdmin(userVO);
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
    @PutMapping("/user/{outerId}/team/null")
    public Map updateUserLeaveTeam(
            @PathVariable("outerId") String outerId
    ) {
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "updateUserLeaveTeam",
                getKV("outerId", outerId)
        ));
        AutoCreateUserVO userVO = new AutoCreateUserVO();
        userVO.setOuterCombineId(outerId);
        autoCreateService.updateUserLeaveTeam(userVO);
        return ResponseResult.success();
    }
}
