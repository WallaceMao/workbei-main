package com.workbei.model.domain.user;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 16:30
 */
public class WbTeamDataDO {
    private Long id;
    private Long version;
    private Long teamId;
    private Date dateCreated;
    private Date lastUpdated;

    //联系人
    private String contacts;
    //手机号
    private String phoneNumber;
    //行业
    private String industry;
    //公司简介
    private String introduction;
    //省市区
    private String region;
    //规模
    private String size;

    //自动注册公司记录的第三方数据
    private String outerId;
    //来自哪个端
    private String client;

    //新需求，用户是否允许售后联系，true:允许，false
    private Boolean canTel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Boolean getCanTel() {
        return canTel;
    }

    public void setCanTel(Boolean canTel) {
        this.canTel = canTel;
    }
}
