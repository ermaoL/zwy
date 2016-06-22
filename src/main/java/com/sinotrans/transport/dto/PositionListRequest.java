package com.sinotrans.transport.dto;


import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.dto.common.CommonListRequest;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emi on 2016/6/15.
 */
public class PositionListRequest extends CommonListRequest {

    private String orderBillNo;//提单号

    private String containerCaseNo;//箱号,柜号

    @Pattern(regexp = "^[1|2]$", message = "进口出口类型格式错误")
    private String orderType;//1: 进口  2:出口

    public String getOrderBillNo() {
        return orderBillNo;
    }

    public void setOrderBillNo(String orderBillNo) {
        this.orderBillNo = orderBillNo;
    }

    public String getContainerCaseNo() {
        return containerCaseNo;
    }

    public void setContainerCaseNo(String containerCaseNo) {
        this.containerCaseNo = containerCaseNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public List fetchFilter(Long userId, CompanyType companyType) throws ParseException {
        return null;
    }

    public Map<String, Object> fetchMapCondition() {
        Map<String, Object> condition = new HashMap<String,Object>();
        condition.put("ordeBillNo", StringUtils.isEmpty(this.orderBillNo) ? "" : this.orderBillNo);
        condition.put("contCaseNo", StringUtils.isEmpty(this.containerCaseNo) ? "" : this.containerCaseNo);
        condition.put("ordeType", StringUtils.isEmpty(this.orderType) ? "" : this.orderType);
        return condition;
    }


}
