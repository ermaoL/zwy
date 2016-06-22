package com.sinotrans.transport.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by emi on 2016/5/27.
 */
public class UserAlterPassRequest {

    @NotEmpty(message = "旧密码不能为空")
    private String oldPass;

    @NotEmpty(message = "新密码不能为空")
    private String newPass;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
