package com.sinotrans.transport.dto;

import org.aspectj.apache.bcel.classfile.Code;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/6/13.
 * 管理员审核用户（）
 */
public class UserVerifierRequest {

    @NotEmpty
    @Pattern(regexp = "^[0|1]$", message = "是否通过格式错误")
    private String ifPass;//1:通过  0:不通过

    @Length(max = 100, message = "不通过的理由最长100个字符")
    private String failedReason;//不通过的理由, 审核不通过需要添加

    @Pattern(regexp = "^[1|2]$", message = "是否通过格式错误")
    private String transCompany;//1:裕雄  2;裕国, 审核通过需要添加

    private String belongCompanyCode;//公司的code, 审核通过需要添加

    @Length(max = 128, message = "公司名称最长128个字符")
    private String belongCompany;//公司名称, 审核通过需要添加

    public String getIfPass() {
        return ifPass;
    }

    public void setIfPass(String ifPass) {
        this.ifPass = ifPass;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public String getTransCompany() {
        return transCompany;
    }

    public void setTransCompany(String transCompany) {
        this.transCompany = transCompany;
    }

    public String getBelongCompanyCode() {
        return belongCompanyCode;
    }

    public void setBelongCompanyCode(String belongCompanyCode) {
        this.belongCompanyCode = belongCompanyCode;
    }

    public String getBelongCompany() {
        return belongCompany;
    }

    public void setBelongCompany(String belongCompany) {
        this.belongCompany = belongCompany;
    }
}
