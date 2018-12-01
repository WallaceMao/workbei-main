package com.workbei.model.domain.user;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class WbDepartmentDO {
    private Long id;
    private String name;

    /*
    * 部门类型，3种：
    * top：和公司名相同的顶级部门
    * unassigned：未分配部门
    * common：除了以上两种之外的部门
    * */
    private String type;

    //部门的等级（最顶级部门为1，以后每级递增）
    private Integer level;
    //顺序值
    private Double displayOrder;
    /*
    * 用于定位上级或下级部门
    * parentId-id
    * 部门有父部门则："${parent.code}-id"
    * 部门没有父部门则：id
    * */
    private String code;

    private Long teamId;
    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
