package com.sinotrans.transport.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/6/1.
 */
public class OrderVerifierRequest {

    @NotEmpty
    @Pattern(regexp = "^[0|1]$", message = "是否通过格式错误")
    private String ifPass;//1:通过  0:不通过

    @Length(max = 100, message = "不通过的理由最长100个字符")
    private String failedReason;//不通过的理由

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
}
