package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.dto.vo.AddressExportSearchRespVo;
import com.sinotrans.transport.dto.vo.ContainerExportSearchRespVo;
import com.sinotrans.transport.dto.vo.OrderExportDetailRespVo;

import java.util.List;

/**
 * Created by emi on 2016/5/31.
 * 出口订单 下发 返回接口(订单状态和箱状态改变了)
 */
public class OrderExportSendResponse extends RestResponse {

    private OrderExportDetailRespVo orderVo;

    private List<ContainerExportSearchRespVo> containerVoList;

    private String firstContainerId;

    private List<AddressExportSearchRespVo> addressVoList;

    public OrderExportSendResponse(OrderExportDetailRespVo orderVo, List<ContainerExportSearchRespVo> containerVoList, Long firstContainerId, List<AddressExportSearchRespVo> addressVoList) {
        this();
        this.orderVo = orderVo;
        this.containerVoList = containerVoList;
        this.firstContainerId = (null == firstContainerId || 0 == firstContainerId) ? "" : String.valueOf(firstContainerId);
        this.addressVoList = addressVoList;
    }

    public OrderExportSendResponse() {
        super();
    }

    public OrderExportDetailRespVo getOrderVo() {
        return orderVo;
    }

    public void setOrderVo(OrderExportDetailRespVo orderVo) {
        this.orderVo = orderVo;
    }

    public List<ContainerExportSearchRespVo> getContainerVoList() {
        return containerVoList;
    }

    public void setContainerVoList(List<ContainerExportSearchRespVo> containerVoList) {
        this.containerVoList = containerVoList;
    }

    public String getFirstContainerId() {
        return firstContainerId;
    }

    public void setFirstContainerId(String firstContainerId) {
        this.firstContainerId = firstContainerId;
    }

    public List<AddressExportSearchRespVo> getAddressVoList() {
        return addressVoList;
    }

    public void setAddressVoList(List<AddressExportSearchRespVo> addressVoList) {
        this.addressVoList = addressVoList;
    }
}
