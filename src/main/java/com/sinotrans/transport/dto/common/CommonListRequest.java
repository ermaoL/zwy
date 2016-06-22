package com.sinotrans.transport.dto.common;

import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.transport.common.enums.CompanyType;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by emi on 2016/5/9.
 */
public abstract class CommonListRequest {

    protected int pageSize;
    protected int currentPage;

    public CommonListRequest(int pageSize, int currentPage) {
        this();
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PagingInfo fetchPagingInfo(){
        return new PagingInfo(pageSize, currentPage);
    }

//    public abstract Map<String, Object> fetchCondition() throws ParseException;

    public abstract List fetchFilter(Long userId, CompanyType companyType) throws ParseException;

    public CommonListRequest() {
        super();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


}
