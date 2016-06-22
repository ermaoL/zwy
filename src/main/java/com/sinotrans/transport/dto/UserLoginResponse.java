package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/11.
 */
public class UserLoginResponse extends RestResponse{

    private String userName;

    private String token;

    private String userCompany;//1:裕雄  2:裕国

    private String userCompanyCode;//用作货主code

    private String userCompanyName;//用作货主

    private String userConnects;//联系人

    private String userConnectEmail;//联系邮箱

    private String userConnectPhone;//联系手机

    public UserLoginResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public UserLoginResponse(String userName, String token, String userCompany, String userCompanyCode, String userCompanyName, String userConnects, String userConnectEmail, String userConnectPhone) {
        this();
        this.userName = userName;
        this.token = token;
        this.userCompany = userCompany;
        this.userCompanyCode = userCompanyCode;
        this.userCompanyName = userCompanyName;
        this.userConnects = userConnects;
        this.userConnectEmail = userConnectEmail;
        this.userConnectPhone = userConnectPhone;
    }

    public UserLoginResponse() {
        super();
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

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserCompanyCode() {
        return userCompanyCode;
    }

    public void setUserCompanyCode(String userCompanyCode) {
        this.userCompanyCode = userCompanyCode;
    }

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    public String getUserConnects() {
        return userConnects;
    }

    public void setUserConnects(String userConnects) {
        this.userConnects = userConnects;
    }

    public String getUserConnectEmail() {
        return userConnectEmail;
    }

    public void setUserConnectEmail(String userConnectEmail) {
        this.userConnectEmail = userConnectEmail;
    }

    public String getUserConnectPhone() {
        return userConnectPhone;
    }

    public void setUserConnectPhone(String userConnectPhone) {
        this.userConnectPhone = userConnectPhone;
    }
}
