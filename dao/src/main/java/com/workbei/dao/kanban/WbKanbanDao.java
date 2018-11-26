package com.workbei.dao.kanban;

import com.workbei.model.kanban.WbKanbanCardDO;
import com.workbei.model.kanban.WbKanbanChildDO;
import com.workbei.model.kanban.WbKanbanDO;
import com.workbei.model.kanban.WbKanbanPreferenceDO;
import com.workbei.model.todo.WbTaskDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:06
 */
@Repository("wbKanbanDao")
public interface WbKanbanDao {
    public void saveOrUpdateKanban(WbKanbanDO kanbanDO);
    public void saveOrUpdateKanbanChild(WbKanbanChildDO kanbanChildDO);
    public void saveOrUpdateKanbanCard(WbKanbanCardDO kanbanCardDO);
    public void saveOrUpdateKanbanTask(WbTaskDO taskDO);
    public void saveOrUpdateKanbanDepartmentMember(
            @Param("kanbanId") Long kanbanId,
            @Param("departmentId") Long deptId,
            @Param("role") String role);
    public void saveOrUpdateKanbanPreference(WbKanbanPreferenceDO kanbanPreferenceDO);
}
