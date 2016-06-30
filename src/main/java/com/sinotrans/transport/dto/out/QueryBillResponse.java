package com.sinotrans.transport.dto.out;

import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.transport.dto.common.CommonListResponse;

import java.util.List;

/**
 * Created by jason on 2016/6/15.
 */
public class QueryBillResponse extends CommonListResponse {

    private String billTotalPrice;  //账单总价

    public String getBillTotalPrice() {
        return billTotalPrice;
    }

    public void setBillTotalPrice(String billTotalPrice) {
        this.billTotalPrice = billTotalPrice;
    }

    public QueryBillResponse(PagingInfo pagingInfo, List data) {
       super(pagingInfo,data);
    }

    public QueryBillResponse(PagingInfo pagingInfo, List data, String billTotalPrice) {
        super(pagingInfo,data);
        this.billTotalPrice = billTotalPrice;
    }

    public QueryBillResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

//    public QueryBillResponse(String billTotalPrice) {
//        this();
//        this.billTotalPrice = billTotalPrice;
//    }
}
