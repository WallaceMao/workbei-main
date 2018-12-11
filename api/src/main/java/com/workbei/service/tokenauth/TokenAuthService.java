package com.workbei.service.tokenauth;

/**
 * @author Wallace Mao
 * Date: 2018-12-11 14:36
 */
public interface TokenAuthService {
    Boolean checkToken(String token);

    String getAppWhiteIpList(String token);
}
