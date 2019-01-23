package com.workbei.service.autocreate;

import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;

import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2019-01-23 14:32
 */
public interface QueryService {
    AutoCreateTeamVO findTeamByOuterId(String client, String outerId);

    List<AutoCreateDepartmentVO> findAllTeamDepartmentsByOuterCorpId(String client, String outerCorpId);

    List<AutoCreateUserVO> findAllTeamStaffsByOuterCorpId(String client, String outerCorpId);
}
