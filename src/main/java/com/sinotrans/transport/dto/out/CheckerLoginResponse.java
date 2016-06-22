package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/31.
 * 裕雄 裕国 登录 返回接口
 */
public class CheckerLoginResponse extends RestResponse{

    private String employeeCode;

    private String employeeName;

    public CheckerLoginResponse() {
        super();
    }

    public String getEmployeeCode() {
        return employeeCode.trim();
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
