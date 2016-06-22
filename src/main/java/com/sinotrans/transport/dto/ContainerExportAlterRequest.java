package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.vo.ContainerExportReqVo;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by emi on 2016/5/19.
 * 出口容器 创建修改 请求接口
 */
public class ContainerExportAlterRequest {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String containerOrderId;//// 订单id，  注意：只有在更新的状况下才使用该字段

    @Valid
    private List<ContainerExportReqVo> containerVoList;

    public ContainerExportAlterRequest(String containerOrderId, List<ContainerExportReqVo> containerVoList) {
        this();
        this.containerOrderId = containerOrderId;
        this.containerVoList = containerVoList;
    }

    public ContainerExportAlterRequest() {
        super();
    }

    public String getContainerOrderId() {
        return containerOrderId;
    }

    public void setContainerOrderId(String containerOrderId) {
        this.containerOrderId = containerOrderId;
    }

    public List<ContainerExportReqVo> getContainerVoList() {
        return containerVoList;
    }

    public void setContainerVoList(List<ContainerExportReqVo> containerVoList) {
        this.containerVoList = containerVoList;
    }
}
