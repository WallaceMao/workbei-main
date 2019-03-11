package com.workbei.service.tokenauth;

import com.workbei.model.domain.user.WbOuterDataAppDO;

/**
 * @author Wallace Mao
 * Date: 2018-12-11 14:36
 */
public interface TokenAuthService {
    WbOuterDataAppDO checkToken(String token);

    String getAppWhiteIpList(String token);
}
