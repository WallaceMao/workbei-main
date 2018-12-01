package com.workbei.model.biz.user;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 11:31
 */
public class DepartmentBO {
    private String name;
    private Double displayOrder;

    private String client;
    private String outerCorpId;
    private String outerParentDeptId;
    private String outerDeptId;
    private String outerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Double displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getOuterCorpId() {
        return outerCorpId;
    }

    public void setOuterCorpId(String outerCorpId) {
        this.outerCorpId = outerCorpId;
    }

    public String getOuterParentDeptId() {
        return outerParentDeptId;
    }

    public void setOuterParentDeptId(String outerParentDeptId) {
        this.outerParentDeptId = outerParentDeptId;
    }

    public String getOuterDeptId() {
        return outerDeptId;
    }

    public void setOuterDeptId(String outerDeptId) {
        this.outerDeptId = outerDeptId;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }
}
