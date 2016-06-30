package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/10.
 */
public class UserRegisterResponse extends RestResponse {

    private String userName;

    private String token;

    public UserRegisterResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UserRegisterResponse() {
        super();
    }

    public UserRegisterResponse(String userName, String token) {
        this();
        this.userName = userName;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
