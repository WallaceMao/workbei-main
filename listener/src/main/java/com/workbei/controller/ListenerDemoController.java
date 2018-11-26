package com.workbei.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-25 18:02
 */
@Controller
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
}
