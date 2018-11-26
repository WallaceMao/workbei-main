package com.workbei.dao.todo;

import com.workbei.model.todo.WbTaskDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-12 1:06
 */
@Repository("wbTodoDao")
public interface WbTodoDao {
    public void saveOrUpdateTodoTask(WbTaskDO taskDO);
    public void saveOrUpdateTaskDate(
            @Param("taskId") Long taskId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    public void saveOrUpdateTaskUserLink(
            @Param("taskId") Long taskId,
            @Param("userId") Long userId
    );
}
