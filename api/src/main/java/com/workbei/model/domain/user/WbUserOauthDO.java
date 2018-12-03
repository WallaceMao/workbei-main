package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * accountId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-27 16:09
 */
public class WbUserOauthDO implements Serializable {
    private Long id;
    private Long version;
    private Long accountId;
    private Date dateCreated;
    private Date lastUpdated;

    //微信用户唯一标识
    private String weixinUnionId;
    //用户微信的名字
    private String weixinName;
    //qq用户唯一标识
    private String qqOpenId;
    //用户qq的名字
    private String qqName;
    //微博用户唯一标识
    private String sinaOpenId;
    //用户微博的名字
    private String sinaName;
    //android小米用户唯一标识
    private String xiaomiOpenId;
    //用户小米的名字
    private String xiaomiName;
    // 钉钉用户唯一标识
    private String ddUnionId;
    // 钉钉用户的名字
    private String ddWebjsonuser;

    //自动注册公司记录的第三方数据
    private String outerId;

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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public String getWeixinUnionId() {
        return weixinUnionId;
    }

    public void setWeixinUnionId(String weixinUnionId) {
        this.weixinUnionId = weixinUnionId;
    }

    public String getWeixinName() {
        return weixinName;
    }

    public void setWeixinName(String weixinName) {
        this.weixinName = weixinName;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getQqName() {
        return qqName;
    }

    public void setQqName(String qqName) {
        this.qqName = qqName;
    }

    public String getSinaOpenId() {
        return sinaOpenId;
    }

    public void setSinaOpenId(String sinaOpenId) {
        this.sinaOpenId = sinaOpenId;
    }

    public String getSinaName() {
        return sinaName;
    }

    public void setSinaName(String sinaName) {
        this.sinaName = sinaName;
    }

    public String getXiaomiOpenId() {
        return xiaomiOpenId;
    }

    public void setXiaomiOpenId(String xiaomiOpenId) {
        this.xiaomiOpenId = xiaomiOpenId;
    }

    public String getXiaomiName() {
        return xiaomiName;
    }

    public void setXiaomiName(String xiaomiName) {
        this.xiaomiName = xiaomiName;
    }

    public String getDdUnionId() {
        return ddUnionId;
    }

    public void setDdUnionId(String ddUnionId) {
        this.ddUnionId = ddUnionId;
    }

    public String getDdWebjsonuser() {
        return ddWebjsonuser;
    }

    public void setDdWebjsonuser(String ddWebjsonuser) {
        this.ddWebjsonuser = ddWebjsonuser;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    @Override
    public String toString() {
        return "WbUserOauthDO{" +
                "id=" + id +
                ", version=" + version +
                ", accountId=" + accountId +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", weixinUnionId='" + weixinUnionId + '\'' +
                ", weixinName='" + weixinName + '\'' +
                ", qqOpenId='" + qqOpenId + '\'' +
                ", qqName='" + qqName + '\'' +
                ", sinaOpenId='" + sinaOpenId + '\'' +
                ", sinaName='" + sinaName + '\'' +
                ", xiaomiOpenId='" + xiaomiOpenId + '\'' +
                ", xiaomiName='" + xiaomiName + '\'' +
                ", ddUnionId='" + ddUnionId + '\'' +
                ", ddWebjsonuser='" + ddWebjsonuser + '\'' +
                ", outerId='" + outerId + '\'' +
                '}';
    }
}
