package com.sinotrans.transport.dto.common;

import com.sinotrans.framework.orm.support.PagingInfo;

import java.util.List;

/**
 * Created by emi on 2016/5/13.
 */
public class CommonListResponse extends RestResponse {

    private PagingInfo pagingInfo;

    private List data;

    public CommonListResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public CommonListResponse(PagingInfo pagingInfo, List data) {
        this();
        this.pagingInfo = pagingInfo;
        this.data = data;
    }

    public CommonListResponse() {
        super();
    }

    public PagingInfo getPagingInfo() {
        return pagingInfo;
    }

    public void setPagingInfo(PagingInfo pagingInfo) {
        this.pagingInfo = pagingInfo;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
