package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.vo.AddressImportReqVo;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by emi on 2016/5/17.
 * 进口订单 创建修改 请求接口
 */
public class AddressImportAlterRequest {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String containerId;//id，  注意：只有在更新的状况下才使用该字段

    @Valid
    private List<AddressImportReqVo> addressVoList;

    public AddressImportAlterRequest() {
        super();
    }

    public AddressImportAlterRequest(String containerId, List<AddressImportReqVo> addressVoList) {
        this();
        this.containerId = containerId;
        this.addressVoList = addressVoList;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public List<AddressImportReqVo> getAddressVoList() {
        return addressVoList;
    }

    public void setAddressVoList(List<AddressImportReqVo> addressVoList) {
        this.addressVoList = addressVoList;
    }
}
