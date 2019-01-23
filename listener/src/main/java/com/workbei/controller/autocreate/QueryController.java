package com.workbei.controller.autocreate;

import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;
import com.workbei.service.autocreate.QueryService;
import com.workbei.util.LogFormatter;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.workbei.util.LogFormatter.getKV;

/**
 * @author Wallace Mao
 * Date: 2019-01-23 23:29
 */
@Api(
        value = "查询组织架构",
        description = "查询组织架构相关操作",
        position = 0,
        produces = "application/json",
        consumes = "application/json",
        tags = "tokenAuth")
@RestController
@RequestMapping("/v3w/tokenAuth")
public class QueryController {
    private static final Logger bizLogger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private QueryService queryService;

    @ApiOperation(value = "查询公司", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, paramType = "header", name = "Authorization", dataType = "string",
                    value = "授权token", defaultValue = "abcd"),
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
    @GetMapping("/team")
    public AutoCreateTeamVO getTeam(
            @RequestParam("client") String client,
            @RequestParam("outerCorpId") String outerCorpId
    ) {
        bizLogger.info(LogFormatter.format(
                LogFormatter.LogEvent.START,
                "getTeam",
                getKV("client", client),
                getKV("outerCorpId", outerCorpId)
        ));
        return queryService.findTeamByOuterId(client, outerCorpId);
    }

    @GetMapping("/departmentList")
    public List<AutoCreateDepartmentVO> listDepartment(
            @RequestParam("client") String client,
            @RequestParam("outerCorpId") String outerCorpId
    ) {
        bizLogger.info(LogFormatter.format(
                LogFormatter.LogEvent.START,
                "listDepartment",
                getKV("client", client),
                getKV("outerCorpId", outerCorpId)
        ));
        return queryService.findAllTeamDepartmentsByOuterCorpId(client, outerCorpId);
    }

    @GetMapping("/userList")
    public List<AutoCreateUserVO> listUser(
            @RequestParam("client") String client,
            @RequestParam("outerCorpId") String outerCorpId
    ) {
        bizLogger.info(LogFormatter.format(
                LogFormatter.LogEvent.START,
                "listUser",
                getKV("client", client),
                getKV("outerCorpId", outerCorpId)
        ));
        return queryService.findAllTeamStaffsByOuterCorpId(client, outerCorpId);
    }
}
