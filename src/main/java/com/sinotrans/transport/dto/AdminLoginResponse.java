package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/31.
 * 管理员与裕雄裕国的审核账户的登录 返回接口
 */
public class AdminLoginResponse extends RestResponse{

    private String adminCode;//管理员code,在请求头中使用

    private String token;//访问令牌,在请求头中使用

    private String authType;//授权类型,在请求头中使用

    private String nickName;//昵称，登陆后 PC端界面展示

    public AdminLoginResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public AdminLoginResponse(String adminCode, String token, String authType, String nickName) {
        this();
        this.adminCode = adminCode;
        this.token = token;
        this.authType = authType;
        this.nickName = nickName;
    }

    public AdminLoginResponse() {
        super();
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
