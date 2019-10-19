package com.example.factory.model.api.account;

import com.example.factory.model.db.User;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:20 2019/10/19
 */
public class AccountRspModel {
    private User user;

    private String   account;

    private String   token;
    // 标识是否已经绑定了设备ID

    private boolean  isBind;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }
}
