package com.workbei.controller.util;

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wallace Mao
 * Date: 2018-12-11 14:51
 */
public class IpUtil {
    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    /**
     * 检查ip是否在指定的子网内
     *
     * @param subNetwork
     * @param ip
     * @return
     */
    public static Boolean checkIpInSubNetwork(String subNetwork, String ip) {
        if (StringUtils.isEmpty(subNetwork) || StringUtils.isEmpty(ip)) {
            return false;
        }
        String[] ipArray = subNetwork.split(",");

        for (String s : ipArray) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            //  特殊ip：0.0.0.0表示允许所有网络
            if ("0.0.0.0".equals(s)) {
                return true;
            }
            SubnetUtils subnetUtil;
            if (s.indexOf("/") > 0) {
                subnetUtil = new SubnetUtils(s);
            } else {
                subnetUtil = new SubnetUtils(s + "/32");
            }
            subnetUtil.setInclusiveHostCount(true);
            SubnetUtils.SubnetInfo info = subnetUtil.getInfo();
            if (info.isInRange(ip)) {
                return true;
            }
        }
        return false;
    }
}
