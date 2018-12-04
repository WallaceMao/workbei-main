package com.workbei.controller;

import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.service.solution.SolutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-25 18:02
 */
@RestController
@RequestMapping("/demo")
public class ListenerDemoController {
    private static final Logger bizLogger = LoggerFactory.getLogger(ListenerDemoController.class);

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        bizLogger.info("info==============" + new Date());
        bizLogger.debug("debug==============" + new Date());
        bizLogger.warn("warn==============" + new Date());
        bizLogger.error("error==============" + new Date());
        return  "success++++";
    }

    @Autowired
    private SolutionService solutionService;
    @RequestMapping("/generateSolution")
    @ResponseBody
    public String generateSolution(
            @RequestParam("teamId") String teamId,
            @RequestParam("userId") String userId,
            @RequestParam("type") String type
    ){
        try {
            if("team".equals(type)){
                solutionService.generateTeamSolution(teamId, userId);
            }else if("staff".equals(type)){
                solutionService.generateUserSolution(teamId, userId);
            }else{
                return "no type";
            }
            return "success";
        } catch (Exception e){
            bizLogger.error("error in demo test", e);
            return "error";
        }
    }

    @RequestMapping(value = "/testBind", method= RequestMethod.POST)
    @ResponseBody
    public AutoCreateTeamVO testBind(
            @RequestParam("client") String client,
            @RequestBody AutoCreateTeamVO autoCreateTeamVO
    ){
                bizLogger.info("=======client: " + client);
                bizLogger.info("=======autoCreateTeamVO: " + autoCreateTeamVO);
                return autoCreateTeamVO;
    }
}
