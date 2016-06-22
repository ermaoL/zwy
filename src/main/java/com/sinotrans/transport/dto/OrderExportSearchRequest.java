package com.sinotrans.transport.dto;


import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.common.enums.DomainStatus;
import com.sinotrans.transport.common.enums.OrderStateType;
import com.sinotrans.transport.common.enums.OrderType;
import com.sinotrans.transport.common.exception.DateFormatException;
import com.sinotrans.transport.dto.common.CommonListRequest;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by emi on 2016/5/13.
 * 出口订单 查询 请求接口
 */
public class OrderExportSearchRequest extends CommonListRequest {

    private String orderBillNo;

    private String orderShipName;

    private String orderSailing;

    private String orderSailingDateFrom;

    private String orderSailingDateTo;

    private String containerCaseNo;

    public Map<String, Object> fetchCondition() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (!StringUtils.isEmpty(this.orderSailingDateFrom)) {
                sdf.parse(this.orderSailingDateFrom);
            }
            if (!StringUtils.isEmpty(this.orderSailingDateTo)) {
                sdf.parse(this.orderSailingDateTo);
            }
        } catch (ParseException e) {
            throw new DateFormatException(sdf, null);
        }
        Map<String, Object> condition = new HashMap<String,Object>();
        condition.put("orderBillNo", this.orderBillNo);
        condition.put("orderShipName", this.orderShipName);
        condition.put("orderSailing", this.orderSailing);
        condition.put("orderSailingDateFrom",this.orderSailingDateFrom);
        condition.put("orderSailingDateTo",this.orderSailingDateTo);
        condition.put("containerCaseNo", this.containerCaseNo);
        return condition;
    }

    public List fetchFilter(Long userId, CompanyType companyType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List filter = new ArrayList();
        if (!StringUtils.isEmpty(this.orderBillNo)) {
            filter.add(new FilterCondition("ordeBillNo", this.orderBillNo, "likeAnywhere"));
        }
        if (!StringUtils.isEmpty(this.orderShipName)) {
            filter.add(new FilterCondition("ordeShipName", this.orderShipName, "likeAnywhere"));
        }
        if (!StringUtils.isEmpty(this.orderSailing)) {
            filter.add(new FilterCondition("ordeSailing", this.orderSailing, "likeAnywhere"));
        }
        try {
            if (!StringUtils.isEmpty(this.orderSailingDateFrom)) {
                filter.add(new FilterCondition("ordeSailingDate", sdf.parse(this.orderSailingDateFrom), ">="));
            }
            if (!StringUtils.isEmpty(this.orderSailingDateTo)) {
                filter.add(new FilterCondition("ordeSailingDate", sdf.parse(this.orderSailingDateTo), "<="));
            }
        } catch (ParseException e) {
            throw new DateFormatException(sdf, null);
        }
        filter.add(new FilterCondition("ordeType", OrderType.Export.getValue(), "="));
        if (null !=  userId && 0 != userId) {
            filter.add(new FilterCondition("creator", String.valueOf(userId), "="));
        }
        if (null != companyType) {
            filter.add(new FilterCondition("ordeState", OrderStateType.Create.getValue(), "!="));
            if (companyType != CompanyType.AdminAll) {
                filter.add(new FilterCondition("ordeTranCompany", companyType.getValue(), "="));
            }
        }
        filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));
        return filter;
    }

    public String getOrderBillNo() {
        return orderBillNo;
    }

    public void setOrderBillNo(String orderBillNo) {
        this.orderBillNo = orderBillNo;
    }

    public String getOrderShipName() {
        return orderShipName;
    }

    public void setOrderShipName(String orderShipName) {
        this.orderShipName = orderShipName;
    }

    public String getOrderSailing() {
        return orderSailing;
    }

    public void setOrderSailing(String orderSailing) {
        this.orderSailing = orderSailing;
    }

    public String getOrderSailingDateTo() {
        return orderSailingDateTo;
    }

    public void setOrderSailingDateTo(String orderSailingDateTo) {
        this.orderSailingDateTo = orderSailingDateTo;
    }

    public String getOrderSailingDateFrom() {
        return orderSailingDateFrom;
    }

    public void setOrderSailingDateFrom(String orderSailingDateFrom) {
        this.orderSailingDateFrom = orderSailingDateFrom;
    }

    public String getContainerCaseNo() {
        return containerCaseNo;
    }

    public void setContainerCaseNo(String containerCaseNo) {
        this.containerCaseNo = containerCaseNo;
    }
}
