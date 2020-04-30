package com.zxf.VO;

import java.io.Serializable;

public class UserVo implements Serializable {

    /**
     * 手机号
     */
    private String telephone;
    /**
     * 验证码
     */
    private String smsCode;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
