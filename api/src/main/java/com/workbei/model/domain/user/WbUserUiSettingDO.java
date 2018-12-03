package com.workbei.model.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * userId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 15:08
 */
public class WbUserUiSettingDO implements Serializable {
    private Long id;
    private Long version;
    private Date dateCreated;
    private Date lastUpdated;
    private Long userId;

    /** 主题皮肤 */
    private String themeSkin;
    /** 背景图 */
    private String themeBg;
    // 收纳箱是否收缩
    private Boolean narrowTheInbox;
    //是否收缩计划侧边栏
    private Boolean narrowTheKanban;
    //是否收缩文集侧边栏
    private Boolean narrowTheCorpus;
    //是否收缩公司侧边栏
    private Boolean narrowTheCompany;
    //是否收缩应用侧边栏
    private Boolean narrowTheApplication;
    //是否提醒用户绑定邮箱或者手机号
    private Boolean registAlertFlag;
    //公司页面下左侧的页面
    private String companyView;
    // 偏好设置中是否开启任务预览
    private Boolean showTipBox;
    // 是否会员到期提醒
    private Boolean expireAlertFlag;
    // 是否显示农历，true:显示，false: 不显示
    private Boolean lunarFlag;
    // 是否显示节假日（默认1，显示）
    private Boolean festivalFlag;
    // 手机端要有每日提醒的字段，到了提醒时间，手机端会主动访问接口获取当天的任务数量，要求是字符串类型，默认值“09:00”
    private String dayAlertTime;
    // 手机端要有修改皮肤的功能，默认值“basic0”
    private String phoneThemeSkin;

    private Boolean firstDeleteFlag;
    //新增字段，收纳箱“完整模式complete，精简模式simple”，新用户默认精简模式,2018年10月8日10:14:12
    private String inboxMode;

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

    public String getThemeSkin() {
        return themeSkin;
    }

    public void setThemeSkin(String themeSkin) {
        this.themeSkin = themeSkin;
    }

    public String getThemeBg() {
        return themeBg;
    }

    public void setThemeBg(String themeBg) {
        this.themeBg = themeBg;
    }

    public Boolean getNarrowTheInbox() {
        return narrowTheInbox;
    }

    public void setNarrowTheInbox(Boolean narrowTheInbox) {
        this.narrowTheInbox = narrowTheInbox;
    }

    public Boolean getNarrowTheKanban() {
        return narrowTheKanban;
    }

    public void setNarrowTheKanban(Boolean narrowTheKanban) {
        this.narrowTheKanban = narrowTheKanban;
    }

    public Boolean getNarrowTheCorpus() {
        return narrowTheCorpus;
    }

    public void setNarrowTheCorpus(Boolean narrowTheCorpus) {
        this.narrowTheCorpus = narrowTheCorpus;
    }

    public Boolean getNarrowTheCompany() {
        return narrowTheCompany;
    }

    public void setNarrowTheCompany(Boolean narrowTheCompany) {
        this.narrowTheCompany = narrowTheCompany;
    }

    public Boolean getNarrowTheApplication() {
        return narrowTheApplication;
    }

    public void setNarrowTheApplication(Boolean narrowTheApplication) {
        this.narrowTheApplication = narrowTheApplication;
    }

    public Boolean getRegistAlertFlag() {
        return registAlertFlag;
    }

    public void setRegistAlertFlag(Boolean registAlertFlag) {
        this.registAlertFlag = registAlertFlag;
    }

    public String getCompanyView() {
        return companyView;
    }

    public void setCompanyView(String companyView) {
        this.companyView = companyView;
    }

    public Boolean getShowTipBox() {
        return showTipBox;
    }

    public void setShowTipBox(Boolean showTipBox) {
        this.showTipBox = showTipBox;
    }

    public Boolean getExpireAlertFlag() {
        return expireAlertFlag;
    }

    public void setExpireAlertFlag(Boolean expireAlertFlag) {
        this.expireAlertFlag = expireAlertFlag;
    }

    public Boolean getLunarFlag() {
        return lunarFlag;
    }

    public void setLunarFlag(Boolean lunarFlag) {
        this.lunarFlag = lunarFlag;
    }

    public Boolean getFestivalFlag() {
        return festivalFlag;
    }

    public void setFestivalFlag(Boolean festivalFlag) {
        this.festivalFlag = festivalFlag;
    }

    public String getDayAlertTime() {
        return dayAlertTime;
    }

    public void setDayAlertTime(String dayAlertTime) {
        this.dayAlertTime = dayAlertTime;
    }

    public String getPhoneThemeSkin() {
        return phoneThemeSkin;
    }

    public void setPhoneThemeSkin(String phoneThemeSkin) {
        this.phoneThemeSkin = phoneThemeSkin;
    }

    public Boolean getFirstDeleteFlag() {
        return firstDeleteFlag;
    }

    public void setFirstDeleteFlag(Boolean firstDeleteFlag) {
        this.firstDeleteFlag = firstDeleteFlag;
    }

    public String getInboxMode() {
        return inboxMode;
    }

    public void setInboxMode(String inboxMode) {
        this.inboxMode = inboxMode;
    }

    @Override
    public String toString() {
        return "WbUserUiSettingDO{" +
                "id=" + id +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", userId=" + userId +
                ", themeSkin='" + themeSkin + '\'' +
                ", themeBg='" + themeBg + '\'' +
                ", narrowTheInbox=" + narrowTheInbox +
                ", narrowTheKanban=" + narrowTheKanban +
                ", narrowTheCorpus=" + narrowTheCorpus +
                ", narrowTheCompany=" + narrowTheCompany +
                ", narrowTheApplication=" + narrowTheApplication +
                ", registAlertFlag=" + registAlertFlag +
                ", companyView='" + companyView + '\'' +
                ", showTipBox=" + showTipBox +
                ", expireAlertFlag=" + expireAlertFlag +
                ", lunarFlag=" + lunarFlag +
                ", festivalFlag=" + festivalFlag +
                ", dayAlertTime='" + dayAlertTime + '\'' +
                ", phoneThemeSkin='" + phoneThemeSkin + '\'' +
                ", firstDeleteFlag=" + firstDeleteFlag +
                ", inboxMode='" + inboxMode + '\'' +
                '}';
    }
}
