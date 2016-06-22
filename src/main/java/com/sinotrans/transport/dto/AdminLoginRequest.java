package com.sinotrans.transport.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/31.
 */
public class AdminLoginRequest {

    @NotEmpty(message = "审核人名字不能为空")
    @Length(max = 32, message = "审核人名字最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "审核人名字格式错误")
    private String adminName;

    @NotEmpty(message = "密码不能为空")
    @Length(max = 32, message = "密码最多32位")
    @Pattern(regexp = "^[0-9a-zA-Z_]{1,32}$", message = "密码格式错误")
    private String adminPass;

    @NotEmpty(message = "审核人类型不能为空")
    @Length(max = 1,message = "审核人类型错误")
    @Pattern(regexp = "^[1-3]$", message = "审核人格式错误")
    private String adminType;//1：裕雄  2：裕国  3:超管

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public String getAdminType() {
        return adminType;
    }

    public void setAdminType(String adminType) {
        this.adminType = adminType;
    }
}
