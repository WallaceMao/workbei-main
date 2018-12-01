package com.workbei.model.domain.user;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 16:08
 */
public class WbOuterDataUserDO {
    private Long id;
    private Long version;
    //创建时间
    private Date dateCreated;
    //最后更新时间
    private Date lastUpdated;

    // user的id
    private Long userId;
    // 外部id，用来做幂等约束
    private String outerId;
    // 外部的标识
    private String client;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
