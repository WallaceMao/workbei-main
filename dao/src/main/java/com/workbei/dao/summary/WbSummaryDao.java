package com.workbei.dao.summary;

import com.workbei.model.summary.WbCorpusDO;
import com.workbei.model.summary.WbSummaryDO;
import com.workbei.model.summary.WbSummaryNoteDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:06
 */
@Repository("wbSummaryDao")
public interface WbSummaryDao {
    public void saveOrUpdateCorpus(WbCorpusDO corpusDO);
    public void saveOrUpdateSummary(WbSummaryDO summaryDO);
    public void saveOrUpdateSummaryNote(WbSummaryNoteDO summaryNoteDO);
    public void saveOrUpdateCorpusDepartmentMember(
            @Param("corpusId") Long corpusId,
            @Param("departmentId") Long deptId,
            @Param("role") String role);
    public void updateSummaryCode(WbSummaryDO summaryDO);
}
