package com.sinotrans.transport.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/11.
 */
public class UserLoginRequest {

    @NotEmpty(message = "用户名不能为空")
    @Length(max = 32, message = "用户名最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "用户名格式错误")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    @Length(max = 32, message = "密码最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "密码格式错误")
    private String userPass;

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
}
