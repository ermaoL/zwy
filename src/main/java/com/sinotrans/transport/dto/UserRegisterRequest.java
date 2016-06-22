package com.sinotrans.transport.dto;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/10.
 */
public class UserRegisterRequest {

    @NotEmpty(message = "用户名不能为空")
    @Length(max = 32, message = "用户名最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "用户名格式错误")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    @Length(max = 32, message = "密码最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "密码格式错误")
    private String userPass;

    @NotEmpty(message = "确认密码不能为空")
    @Length(max = 32, message = "确认密码最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "确认密码格式错误")
    private String userPassConfirm;

    @NotEmpty(message = "手机号不能为空")
    @Length(max = 11, message = "手机号最多32位")
    private String userTel;
//
//    @NotEmpty(message = "公司Id不能为空")
//    @Length(max = 64, message = "公司做多64位")
//    private String userCompanyId;

    @NotEmpty(message = "公司不能为空")
    @Length(max = 64, message = "公司做多64位")
    private String userCompany;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserPassConfirm() {
        return userPassConfirm;
    }

    public void setUserPassConfirm(String userPassConfirm) {
        this.userPassConfirm = userPassConfirm;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public boolean equalPass() {
        if (this.userPass.equals(this.userPassConfirm)) {
            return true;
        } else {
            return false;
        }
    }

}
