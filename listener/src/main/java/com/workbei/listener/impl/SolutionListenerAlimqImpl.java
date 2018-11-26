package com.workbei.listener.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.workbei.event.SolutionEvent;
import com.workbei.exception.WorkbeiRuntimeException;
import com.workbei.listener.SolutionListener;
import com.workbei.service.SolutionBizService;
import com.workbei.util.LogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-11-21 15:29
 */
public class SolutionListenerAlimqImpl implements MessageListener, SolutionListener {
    private static final Logger bizLogger = LoggerFactory.getLogger(SolutionListenerAlimqImpl.class);

    @Autowired
    private SolutionBizService solutionBizService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try{
            SolutionEvent event = JSON.parseObject(message.getBody(), SolutionEvent.class);
            String teamId = event.getTeamId();
            String userId = event.getUserId();
            String type = event.getType();

            if("team".equals(type)){
                solutionBizService.generateTeamSolution(teamId, userId);
            }else if("staff".equals(type)){
                solutionBizService.generateUserSolution(teamId, userId);
            }else{
                throw new WorkbeiRuntimeException("invalid solution type: " + type + ", teamId: " + teamId + ", userId: " + userId);
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            bizLogger.error(LogFormatter.format(
                    LogFormatter.LogEvent.EXCEPTION,
                    "SolutionListenerAlimqImpl",
                    new LogFormatter.KeyValue("message", message)
            ), e);
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
