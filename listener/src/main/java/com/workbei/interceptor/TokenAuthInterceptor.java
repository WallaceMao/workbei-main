package com.workbei.interceptor;

import com.workbei.controller.util.IpUtil;
import com.workbei.model.domain.user.WbOuterDataAppDO;
import com.workbei.service.tokenauth.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wallace Mao
 * Date: 2018-12-08 16:05
 */
public class TokenAuthInterceptor extends HandlerInterceptorAdapter {
    private TokenAuthService tokenAuthService;

    @Autowired
    public TokenAuthInterceptor(TokenAuthService tokenAuthService) {
        this.tokenAuthService = tokenAuthService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        // 检查是否为空
        if (StringUtils.isEmpty(auth)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 检查token在数据库中是否存在
        WbOuterDataAppDO outerDataAppDO = tokenAuthService.checkToken(auth);
        if (outerDataAppDO == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 检查ip是否在白名单中
        String whiteIpList = tokenAuthService.getAppWhiteIpList(auth);
        if (!IpUtil.checkIpInSubNetwork(whiteIpList, IpUtil.getRequestIp(request))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        request.setAttribute("outerClient", outerDataAppDO.getKey());
        return true;
    }
}
