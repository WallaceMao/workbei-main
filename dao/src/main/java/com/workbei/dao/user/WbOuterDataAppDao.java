package com.workbei.dao.user;

import com.workbei.model.domain.user.WbOuterDataAppDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:15
 */
@Repository("wbOuterDataAppDao")
public interface WbOuterDataAppDao {
    WbOuterDataAppDO getOuterDataAppByToken(
            @Param("token") String token
    );
}
