package com.workbei.service.biz.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.workbei.constant.WbConstant;
import com.workbei.dao.kanban.WbKanbanDao;
import com.workbei.dao.summary.WbSummaryDao;
import com.workbei.dao.system.WbSolutionDao;
import com.workbei.dao.todo.WbTodoDao;
import com.workbei.dao.user.WbDepartmentDao;
import com.workbei.dao.user.WbUserDao;
import com.workbei.model.kanban.WbKanbanCardDO;
import com.workbei.model.kanban.WbKanbanChildDO;
import com.workbei.model.kanban.WbKanbanDO;
import com.workbei.model.kanban.WbKanbanPreferenceDO;
import com.workbei.model.summary.WbCorpusDO;
import com.workbei.model.summary.WbSummaryDO;
import com.workbei.model.summary.WbSummaryNoteDO;
import com.workbei.model.system.WbSolutionDO;
import com.workbei.model.todo.WbTaskDO;
import com.workbei.service.biz.SolutionBizService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-26 11:06
 */
public class SolutionBizServiceImpl implements SolutionBizService {
    private static final String WORKBEI_TYPE_TEAM = "com.workbei-team";
    private static final String WORKBEI_TYPE_STAFF = "com.workbei-staff";
    private static final Long ORDER_START = 65535L;
    private static final Long ORDER_STEP = 65536L;
    @Autowired
    private WbSolutionDao wbSolutionDao;
    @Autowired
    private WbUserDao wbUserDao;
    @Autowired
    private WbDepartmentDao wbDepartmentDao;
    @Autowired
    private WbKanbanDao wbKanbanDao;
    @Autowired
    private WbSummaryDao wbSummaryDao;
    @Autowired
    private WbTodoDao wbTodoDao;

    /**
     * 生成解决方案
     * @param teamId
     * @param userId
     */
    public void generateTeamSolution(String teamId, String userId){
        List<WbSolutionDO> list = wbSolutionDao.getWbSolutionListByType(WORKBEI_TYPE_TEAM);
        Long targetTeamId = Long.valueOf(teamId);
        Long targetUserId = Long.valueOf(userId);
        generateSolution(targetTeamId, targetUserId, list);
    }

    public void generateUserSolution(String teamId, String userId){
        List<WbSolutionDO> list = wbSolutionDao.getWbSolutionListByType(WORKBEI_TYPE_STAFF);
        Long targetTeamId = Long.valueOf(teamId);
        Long targetUserId = Long.valueOf(userId);
        generateSolution(targetTeamId, targetUserId, list);
    }

    private Long getTopDeptId(Long teamId){
        return wbDepartmentDao.getTopDepartmentId(teamId);
    }

    private void generateSolution(Long teamId, Long userId, List<WbSolutionDO> list){
        if(list == null){
            return;
        }
        for(WbSolutionDO solutionDO : list){
            if(solutionDO.getTodo() != null){
                parseAndGenerateTodo(teamId, userId, JSONArray.parseArray(solutionDO.getTodo()));
            }
            if(solutionDO.getCorpus() != null){
                parseAndGenerateCorpus(teamId, userId, JSONArray.parseArray(solutionDO.getCorpus()));
            }
            if(solutionDO.getKanban() != null){
                parseAndGenerateKanban(teamId, userId, JSONArray.parseArray(solutionDO.getKanban()));
            }
        }
    }

    private void parseAndGenerateTodo(Long teamId, Long userId, JSONArray array){
        if(array == null){
            return;
        }
        for(int i = 0; i < array.size(); i++){
            JSONObject jsonTodo = array.getJSONObject(i);
            WbTaskDO task = getDefaultTask();
            task.setCreatorId(userId);
            task.setName(jsonTodo.getString("name"));
            task.setNote(jsonTodo.getString("note"));
            //处理type
            task.setType(1L);
            //处理displayOrder
            task.setDisplayOrder((double)ORDER_START + i * ORDER_STEP);
            wbTodoDao.saveOrUpdateTodoTask(task);
            wbTodoDao.saveOrUpdateTaskDate(task.getId(), getNowDate(), getNowDate());
            wbTodoDao.saveOrUpdateTaskUserLink(task.getId(), userId);
        }
    }

    private void parseAndGenerateKanban(Long teamId, Long userId, JSONArray array){
        if(array == null){
            return;
        }

        Long topDeptId = getTopDeptId(teamId);
        for(int i = 0; i < array.size(); i++){
            JSONObject jsonKanban = array.getJSONObject(i);
            WbKanbanDO kanban = getDefaultKanban();
            kanban.setName(jsonKanban.getString("name"));
            kanban.setCover(jsonKanban.getString("cover"));
            wbKanbanDao.saveOrUpdateKanban(kanban);
            WbKanbanChildDO kanbanChild = null;
            if(jsonKanban.containsKey("kanbanChild")){
                JSONArray jsonKanbanChildArray = jsonKanban.getJSONArray("kanbanChild");
                for(int j = 0; j < jsonKanbanChildArray.size(); j++){
                    JSONObject jsonKanbanChild = jsonKanbanChildArray.getJSONObject(j);
                    kanbanChild = new WbKanbanChildDO();
                    kanbanChild.setName(jsonKanbanChild.getString("name"));
                    kanbanChild.setKanbanId(kanban.getId());
                    kanbanChild.setDisplay(true);
                    kanbanChild.setDisplayOrder((double)ORDER_START + j * ORDER_STEP);
                    wbKanbanDao.saveOrUpdateKanbanChild(kanbanChild);

                    if(jsonKanbanChild.containsKey("kanbanCard")){
                        JSONArray jsonKanbanCardArray = jsonKanbanChild.getJSONArray("kanbanCard");
                        for(int k = 0; k < jsonKanbanCardArray.size(); k++){
                            JSONObject jsonKanbanCard = jsonKanbanCardArray.getJSONObject(k);
                            WbKanbanCardDO card = new WbKanbanCardDO();
                            card.setName(jsonKanbanCard.getString("name"));
                            card.setKanbanChildId(kanbanChild.getId());
                            card.setDisplay(true);
                            card.setDisplayOrder((double)ORDER_START + k * ORDER_STEP);
                            wbKanbanDao.saveOrUpdateKanbanCard(card);

                            if(jsonKanbanCard.containsKey("task")){
                                JSONArray jsonTaskArray = jsonKanbanCard.getJSONArray("task");
                                for(int m = 0; m < jsonTaskArray.size(); m++){
                                    JSONObject jsonTask = jsonTaskArray.getJSONObject(m);
                                    WbTaskDO task = getDefaultTask();
                                    task.setCreatorId(userId);
                                    task.setKanbanCardId(card.getId());
                                    task.setName(jsonTask.getString("name"));
                                    task.setNote(jsonTask.getString("note"));
                                    task.setDisplayOrder((double)ORDER_START + m * ORDER_STEP);
                                    wbKanbanDao.saveOrUpdateKanbanTask(task);
                                }
                            }
                        }
                    }

                }
            }
            wbKanbanDao.saveOrUpdateKanbanDepartmentMember(kanban.getId(), topDeptId, WbConstant.KANBAN_DEFAULT_ROLE);
            //  保存kanbanPreference
            WbKanbanPreferenceDO preferenceDO = getDefaultKanbanPreference();
            preferenceDO.setKanbanId(kanban.getId());
            preferenceDO.setDefaultKanbanChildId(kanbanChild.getId());
            wbKanbanDao.saveOrUpdateKanbanPreference(preferenceDO);
        }
    }

    private void parseAndGenerateCorpus(Long teamId, Long userId, JSONArray array){
        if(array == null){
            return;
        }
        Long topDeptId = getTopDeptId(teamId);
        for(int i = 0; i < array.size(); i++){
            JSONObject jsonCorpus = array.getJSONObject(i);
            WbCorpusDO corpusDO = getDefaultCorpus();
            corpusDO.setName(jsonCorpus.getString("name"));
            corpusDO.setCover(jsonCorpus.getString("cover"));
            wbSummaryDao.saveOrUpdateCorpus(corpusDO);
            wbSummaryDao.saveOrUpdateCorpusDepartmentMember(corpusDO.getId(), topDeptId, WbConstant.SUMMARY_DEFAULT_ROLE);

            if(jsonCorpus.containsKey("summary")){
                saveRecursiveSummary(
                        teamId,
                        userId,
                        corpusDO.getId(),
                        null,
                        corpusDO.getId().toString(),
                        jsonCorpus.getJSONArray("summary"));
            }
        }
    }

    private void saveRecursiveSummary(Long teamId, Long userId, Long corpusId, Long parentSummaryId, String idChain, JSONArray jsonSummaryArray){
        for(int i = 0; i < jsonSummaryArray.size(); i++){
            JSONObject jsonSummary = jsonSummaryArray.getJSONObject(i);
            WbSummaryDO summary = getDefaultSummary();
            summary.setName(jsonSummary.getString("name"));
            summary.setNoteBook(jsonSummary.getBoolean("isNoteBook"));
            summary.setDisplayOrder((double)ORDER_START + i * ORDER_STEP);
            summary.setCorpusId(corpusId);
            summary.setNoteBookId(parentSummaryId);
            summary.setCode(idChain);
            wbSummaryDao.saveOrUpdateSummary(summary);
            //  更新code
            String currentChain = idChain + "-" + summary.getId();
            summary.setCode(currentChain);
            wbSummaryDao.updateSummaryCode(summary);
            //  如果summaryNote存在，就先保存note
            if(jsonSummary.containsKey("note")){
                WbSummaryNoteDO note = new WbSummaryNoteDO();
                note.setSummaryId(summary.getId());
                note.setNote(jsonSummary.getString("note"));
                wbSummaryDao.saveOrUpdateSummaryNote(note);
            }
            //  如果子文件夹存在，那么就递归调用保存子文件夹
            if(jsonSummary.containsKey("summary")){
                JSONArray childSummaryArray = jsonSummary.getJSONArray("summary");
                saveRecursiveSummary(teamId, userId, corpusId, summary.getId(), currentChain, childSummaryArray);
            }
        }
    }

    private WbTaskDO getDefaultTask(){
        WbTaskDO task = new WbTaskDO();
        task.setSync(false);
        task.setForbidEdit(false);
        task.setDisplay(true);
        task.setOpenToMember(false);
        task.setProgress(0L);
        task.setPriority(1L);
        task.setInbox(false);
        //  日程任务要改成1
        task.setType(2L);
        return task;
    }

    private WbKanbanDO getDefaultKanban(){
        WbKanbanDO kanban = new WbKanbanDO();
        kanban.setDefaultRole(WbConstant.KANBAN_DEFAULT_ROLE);
        kanban.setAttribute(WbConstant.KANBAN_ATTRIBUTE_COMPANY);
        kanban.setDisplay(true);
        return kanban;
    }

    private WbKanbanPreferenceDO getDefaultKanbanPreference(){
        WbKanbanPreferenceDO pre = new WbKanbanPreferenceDO();
        pre.setAddCreatorAsMember(false);
        pre.setSyncTask(WbConstant.KANBAN_SYNC_TASK_TO_TODO);
        pre.setOnlyMemberCanSee(false);
        pre.setKanbanChildMode(WbConstant.KANBAN_CHILD_MODE_FREE);
        pre.setTaskSinking(true);
        pre.setShowPriority(true);
        return pre;
    }

    private WbCorpusDO getDefaultCorpus(){
        WbCorpusDO corpus = new WbCorpusDO();
        corpus.setDefaultRole(WbConstant.SUMMARY_DEFAULT_ROLE);
        corpus.setCanBeShared(true);
        corpus.setAttribute(WbConstant.SUMMARY_ATTRIBUTE_COMPANY);
        corpus.setType(WbConstant.SUMMARY_ESSAY_TYPE);
        corpus.setDisplay(true);
        corpus.setAllowAddBeforeDoc(true);
        corpus.setAllowEditBeforeDoc(true);
        return corpus;
    }

    private WbSummaryDO getDefaultSummary(){
        WbSummaryDO summary = new WbSummaryDO();
        summary.setOpenToMember(false);
        summary.setDate(WbConstant.SUMMARY_ESSAY_NOTE_DATE);
        summary.setSystem(false);
        summary.setShare(false);
        summary.setDisplay(true);
        return summary;
    }

    private Date getNowDate(){
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
//        long time = cal.getTimeInMillis();
    }
}
