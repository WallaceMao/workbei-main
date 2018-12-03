package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * accountId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 9:30
 */
public class WbUserRegisterDO implements Serializable {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    private Date regDate;
    private Long accountId;
    private String mode;
    private String client;
    private Boolean isSystem;

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

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    @Override
    public String toString() {
        return "WbUserRegisterDO{" +
                "id=" + id +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", regDate=" + regDate +
                ", accountId=" + accountId +
                ", mode='" + mode + '\'' +
                ", client='" + client + '\'' +
                ", isSystem=" + isSystem +
                '}';
    }
}
