package com.sinotrans.transport.common.cache;

/**
 * Created by emi on 2016/5/31.
 */
public class AdminResultVo {

    private String token;

    private String adminName;//tokenCacheAdmin-adminCode

    private String authType;

    private String nickName;//tokenCacheAdmin-adminName

    public AdminResultVo(String token, String adminName, String authType, String nickName) {
        this();
        this.token = token;
        this.adminName = adminName;
        this.authType = authType;
        this.nickName = nickName;
    }

    public AdminResultVo() {
        super();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
