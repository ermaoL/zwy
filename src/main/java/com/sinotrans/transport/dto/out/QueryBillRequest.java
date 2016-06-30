package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.dto.common.CommonListRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by emi on 2016/6/29.
 */
public class QueryBillRequest extends CommonListRequest {

    private String beginTime;//开始时间  //beginEtd

    private String endTime;//结束时间    //endEtd

    private String orderType;//进出口     //importExport

    private String orderBillNo;//提单号    //claimNo

    private String containerNo;//箱号     //containerNo

//    private String crmCode;//管理员编号

    @Override
    public List fetchFilter(Long userId, CompanyType companyType) {
        return null;
    }

    @Override
    public Map<String, Object> fetchCondition() {
        return null;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderBillNo() {
        return orderBillNo;
    }

    public void setOrderBillNo(String orderBillNo) {
        this.orderBillNo = orderBillNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }
}
