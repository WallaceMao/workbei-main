package com.workbei.model.domain.user;

/**
 * userId为唯一约束
 * @author Wallace Mao
 * Date: 2018-11-28 15:09
 */
public class WbUserFunctionSettingDO {
    private Long id;
    private Long version;
    private Long userId;
    //移动端星期开始于 功能
    private String weekBegins;
    //android通知栏插件
    private Boolean isOpenNotificationPlugin;
    //免费版用户能够读取同事数据的次数
    private Integer readCollageCount;
    //移动端震动模式
    private Boolean shakeMode;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWeekBegins() {
        return weekBegins;
    }

    public void setWeekBegins(String weekBegins) {
        this.weekBegins = weekBegins;
    }

    public Boolean getOpenNotificationPlugin() {
        return isOpenNotificationPlugin;
    }

    public void setOpenNotificationPlugin(Boolean openNotificationPlugin) {
        isOpenNotificationPlugin = openNotificationPlugin;
    }

    public Integer getReadCollageCount() {
        return readCollageCount;
    }

    public void setReadCollageCount(Integer readCollageCount) {
        this.readCollageCount = readCollageCount;
    }

    public Boolean getShakeMode() {
        return shakeMode;
    }

    public void setShakeMode(Boolean shakeMode) {
        this.shakeMode = shakeMode;
    }
}
