package com.workbei.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.workbei.model.view.autocreate.AutoCreateDepartmentVO;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.model.view.autocreate.AutoCreateUserVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 20:14
 */
public class AutoCreateConverter {
    public static AutoCreateTeamVO json2Team(JSONObject json){
        if(json == null){
            return null;
        }
        AutoCreateTeamVO team = new AutoCreateTeamVO();
        if(json.containsKey("name")){
            team.setName(json.getString("name"));
        }
        if(json.containsKey("logo")){
            team.setLogo(json.getString("logo"));
        }
        if(json.containsKey("outerCorpId")){
            team.setOuterCorpId(json.getString("outerCorpId"));
        }
        if(json.containsKey("client")){
            team.setClient(json.getString("client"));
        }
        if(json.containsKey("creator")){
            team.setCreator(json2User(json.getJSONObject("creator")));
        }
        return team;
    }

    public static AutoCreateDepartmentVO json2Deptartment(JSONObject json){
        if(json == null){
            return null;
        }
        AutoCreateDepartmentVO dept = new AutoCreateDepartmentVO();
        if(json.containsKey("name")){
            dept.setName(json.getString("name"));
        }
        if(json.containsKey("orderNum")){
            dept.setDisplayOrder(json.getDouble("orderNum"));
        }
        if(json.containsKey("client")){
            dept.setClient(json.getString("client"));
        }
        if(json.containsKey("outerCorpId")){
            dept.setOuterCorpId(json.getString("outerCorpId"));
        }
        if(json.containsKey("outerParentCombineId")){
            dept.setOuterParentCombineId(json.getString("outerParentCombineId"));
        }
        if(json.containsKey("outerDeptId")){
            dept.setOuterCombineId(json.getString("outerCombineId"));
        }

        return dept;
    }

    public static AutoCreateUserVO json2User(JSONObject json){
        if(json == null){
            return null;
        }
        AutoCreateUserVO user = new AutoCreateUserVO();
        if(json.containsKey("realName")){
            user.setName(json.getString("realName"));
        }
        if(json.containsKey("avatar")){
            user.setAvatar(json.getString("avatar"));
        }
        if(json.containsKey("client")){
            user.setClient(json.getString("client"));
        }
        if(json.containsKey("outerCorpId")){
            user.setOuterCorpId(json.getString("outerCorpId"));
        }
        if(json.containsKey("outerUserId")){
            user.setOuterUserId(json.getString("outerUserId"));
        }
        if(json.containsKey("outerCombineId")){
            user.setOuterCombineId(json.getString("outerCombineId"));
        }
        if(json.containsKey("outerUnionId")){
            user.setOuterUnionId(json.getString("outerUnionId"));
        }
        if(json.containsKey("outerDepartment")){
            JSONArray array = json.getJSONArray("outerDepartment");
            List<String> deptIdList = new ArrayList<>(array.size());
            for(int i = 0; i < array.size(); i++){
                String deptId = array.getString(i);
                deptIdList.add(deptId);
            }
            user.setOuterCombineDeptIdList(deptIdList);
        }
        return user;
    }

    public static AutoCreateUserVO generateTeamCreator(AutoCreateTeamVO teamVO){
        AutoCreateUserVO userVO = new AutoCreateUserVO();
        userVO.setClient(teamVO.getClient());
        userVO.setName(teamVO.getName() + "----管理员");
        userVO.setOuterCombineId(teamVO.getOuterCorpId() + "----creator");
        return userVO;
    }
}
