package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.vo.ContainerImportReqVo;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by emi on 2016/5/17.
 * 进口容器 创建修改 请求接口
 */
public class ContainerImportAlterRequest {

    private String containerOrderId;// 订单id，  注意：只有在更新的状况下才使用该字段

    @Valid
    private List<ContainerImportReqVo> containerVoList;

    public ContainerImportAlterRequest() {
        super();
    }

    public ContainerImportAlterRequest(String containerOrderId, List<ContainerImportReqVo> containerVoList) {
        this();
        this.containerOrderId = containerOrderId;
        this.containerVoList = containerVoList;
    }

    public String getContainerOrderId() {
        return containerOrderId;
    }

    public void setContainerOrderId(String containerOrderId) {
        this.containerOrderId = containerOrderId;
    }

    public List<ContainerImportReqVo> getContainerVoList() {
        return containerVoList;
    }

    public void setContainerVoList(List<ContainerImportReqVo> containerVoList) {
        this.containerVoList = containerVoList;
    }
}
