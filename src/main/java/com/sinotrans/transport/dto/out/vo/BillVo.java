package com.sinotrans.transport.dto.out.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/6/16.
 *
 */
public class BillVo {

    private String docCode;

    private List<Map<String, String>> bills = new ArrayList();

    public BillVo(String docCode, List bills) {
        this.docCode = docCode;
        this.bills = bills;
    }

    public BillVo() {
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public List<Map<String, String>> getBills() {
        return bills;
    }

    public void setBills(List<Map<String, String>> bills) {
        this.bills = bills;
    }
}
