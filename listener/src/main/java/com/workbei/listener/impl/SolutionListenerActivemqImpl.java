package com.workbei.listener.impl;

import com.workbei.exception.WorkbeiRuntimeException;
import com.workbei.listener.SolutionListener;
import com.workbei.service.biz.SolutionBizService;
import com.workbei.util.LogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 5:35
 */
public class SolutionListenerActivemqImpl implements MessageListener, SolutionListener {
    private static final Logger bizLogger = LoggerFactory.getLogger(SolutionListenerActivemqImpl.class);

    @Autowired
    private SolutionBizService solutionBizService;

    @Override
    public void onMessage(Message message) {
        try{
            MapMessage mapMessage = (MapMessage)message;
            String teamId = mapMessage.getString("teamId");
            String userId = mapMessage.getString("userId");
            String type = mapMessage.getString("type");

            if("team".equals(type)){
                solutionBizService.generateTeamSolution(teamId, userId);
            }else if("staff".equals(type)){
                solutionBizService.generateUserSolution(teamId, userId);
            }else{
                throw new WorkbeiRuntimeException("invalid solution type: " + type + ", teamId: " + teamId + ", userId: " + userId);
            }
        } catch (Exception e) {
            bizLogger.error(LogFormatter.format(
                    LogFormatter.LogEvent.EXCEPTION,
                    "SolutionListenerAlimqImpl",
                    new LogFormatter.KeyValue("message", message)
            ), e);
        }
    }
}
