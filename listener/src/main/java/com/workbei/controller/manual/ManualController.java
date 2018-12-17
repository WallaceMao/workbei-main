package com.workbei.controller.manual;

import com.workbei.service.solution.SolutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 18:01
 */
@Controller
@RequestMapping("/manual")
public class ManualController {
    private static final Logger bizLogger = LoggerFactory.getLogger(ManualController.class);
    @Autowired
    private SolutionService solutionService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public String ping(){
        bizLogger.info("----pong----");
        return "pong";
    }

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
}
