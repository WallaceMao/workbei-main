package com.workbei.dao.system;

import com.workbei.model.system.WbSolutionDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 3:06
 */
@Repository("wbSolutionDao")
public interface WbSolutionDao {
    List<WbSolutionDO> getWbSolutionListByType(@Param("type") String type);
}
