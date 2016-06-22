package com.sinotrans.transport.dto;

import java.util.List;

/**
 * Created by emi on 2016/5/20.
 * 进口容器 删除 请求接口
 */
public class DomainsDropRequest {

    List<String> idList;

    public DomainsDropRequest(List<String> idList) {
        this();
        this.idList = idList;
    }

    public DomainsDropRequest() {
        super();
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
