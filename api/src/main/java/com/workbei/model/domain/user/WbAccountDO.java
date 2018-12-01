package com.workbei.model.domain.user;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-11-27 15:20
 */
public class WbAccountDO {
    private Long id;
    private Long version;
    //帐号是否被锁住
    private boolean accountLocked;
    //邮箱
    private String email;
    //是否验证过邮箱
    private Boolean isCheckEmail;
    private String password;
    //手机号
    private String phoneNumber;
    //头像地址后缀，不能直接返回给客户端，需要经过getUserAvatar()方法处理一下
    private String avatar;
    //个人设置改的名字
    private String name;
    //生日
    private Date birth;

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

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getCheckEmail() {
        return isCheckEmail;
    }

    public void setCheckEmail(Boolean checkEmail) {
        isCheckEmail = checkEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "AccountVO{" +
                "accountLocked=" + accountLocked +
                ", email='" + email + '\'' +
                ", isCheckEmail=" + isCheckEmail +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                '}';
    }
}
