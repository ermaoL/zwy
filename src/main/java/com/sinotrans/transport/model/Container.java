package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.enums.ContainerStateType;
import com.sinotrans.transport.common.enums.DomainStatus;
import com.sinotrans.transport.common.enums.DomainType;
import com.sinotrans.transport.common.exception.ContainerStateException;
import com.sinotrans.transport.common.exception.DomainNotBelongException;
import com.sinotrans.transport.common.exception.RecordHasDeleteException;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by emi on 2016/5/12.
 */
@Entity
@Table(name = "CONTAINER")
public class Container extends BaseModel implements OperationLog {

    private Long contId;

    private Long contOrdeId;

    private String contOwner;//箱主                           ,import      todo:进出口都有？

    private String contOwnerCode;//箱主代码                    ,import    //new todo:进出口都有？

    private String contCaseNo;//箱号                     export,import     todo:唯一否

    private String contGbsId;//GBS中箱表中的主键         export,import         //new

    private Integer contCount;//数量                     export,import

    private String contDrayageDepot;//提箱点               export,import
    private String contDrayageDepotCode;//提箱点               export,import

    private String contReturnDepot;//返箱点                export,import
    private String contReturnDepotCode;//返箱点                export,import

    private Integer contState;//状态                      export,import

    private String contSealNo;//铅封号                         ,import

    private String contTradeName;//品名                      ,import

    private String contType;//箱型                       export,import

    private String contCartonSize;//尺寸                export,import

    private Double contGrossWeight;//毛重               export,import

    private Integer contRfTemp;//冻柜温度                export,import          //todo：delete

    private Integer contTemperUnit;//温度类型 0:华氏T 1:摄氏C export,import     //todo：delete

    private String contDynamo;//是否带电机                 export,import

    private String contIsMoreUnload;//否多地卸           export,import          //出口是是否多地装

    private String contIsOverWeight;//是否超重            export

    private String contIsCheck;//是否柜检                 export

    private String contIsCheckliest;//是否需要柜检单      export

    private String contIsReplace;//是否冻代干              export

    private Double contCost;//12,2  费用                         ,import

    private String contOther;//其他                          export,import

    private String contRemark;//送柜要求及备注                export,import

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Long recVer;//数据行版本号

    private Integer recStatus;// 0:正常   1：删除

    public Container() {
        super();
        this.recVer = 0L;
        this.recStatus = 0;
//        this.contIsSend = 0;
    }

    public Container(Long contOrderId, String contOwner, String contOwnerCode, String contCaseNo, String contCount, String contDrayageDepot, String contDrayageDepotCode, String contReturnDepot, String contReturnDepotCode, String contSealNo, String contTradeName, String contType, String contCartonSize, String contGrossWeight, String contRfTemp, String contTemperUnit, String contDynamo, String contIsMoreUnload, String contCost, String contOther, String contRemark, String creator) {
        this();
        this.contState = ContainerStateType.ImportCreate.getValue();

        this.contOrdeId = contOrderId;
        this.contOwner = contOwner;
        this.contOwnerCode = contOwnerCode;
        this.contCaseNo = contCaseNo;
        this.contCount = StringUtils.isEmpty(contCount) ? 0 : Integer.parseInt(contCount);
        this.contDrayageDepot = contDrayageDepot;
        this.contDrayageDepotCode = contDrayageDepotCode;
        this.contReturnDepot = contReturnDepot;
        this.contReturnDepotCode = contReturnDepotCode;
        this.contSealNo = contSealNo;
        this.contTradeName = contTradeName;
        this.contType = contType;
        this.contCartonSize = contCartonSize;
        this.contGrossWeight = StringUtils.isEmpty(contGrossWeight) ? null : Double.parseDouble(contGrossWeight);
        this.contDynamo = contDynamo;
//        if (this.contDynamo.equals("是")) {
//            this.contRfTemp = Integer.parseInt(contRfTemp);
//            this.contTemperUnit = Integer.parseInt(contTemperUnit) > 0 ? 1 : 0;
//        }
        this.contIsMoreUnload = contIsMoreUnload;
        this.contCost = StringUtils.isEmpty(contCost) ? null : new BigDecimal(contCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.contOther = contOther;
        this.contRemark = contRemark;
        this.creator = creator;
    }

    public Container(String contOwner, String contOwnerCode, Long contOrdeId, String contCaseNo, String contCount, String contDrayageDepot, String contDrayageDepotCode, String contReturnDepot, String contReturnDepotCode, String contGrossWeight, String contRfTemp, String contTemperUnit, String contType, String contCartonSize, String contDynamo, String contIsMoreUnload, String contIsOverWeight, String contIsCheck, String contIsCheckliest, String contIsReplace, String contOther, String contRemark, String creator) {
        this();
        this.contState = ContainerStateType.ExportCreate.getValue();

        this.contOrdeId = contOrdeId;
        this.contOwner = contOwner;
        this.contOwnerCode = contOwnerCode;
        this.contCaseNo = contCaseNo;
        this.contCount = StringUtils.isEmpty(contCount) ? 0 : Integer.parseInt(contCount);
        this.contDrayageDepot = contDrayageDepot;
        this.contDrayageDepotCode = contDrayageDepotCode;
        this.contReturnDepot = contReturnDepot;
        this.contReturnDepotCode = contReturnDepotCode;
        this.contGrossWeight = StringUtils.isEmpty(contGrossWeight) ? null : Double.parseDouble(contGrossWeight);
        this.contType = contType;
        this.contCartonSize = contCartonSize;
        this.contDynamo = contDynamo;
//        if (this.contDynamo.equals("是")) {
//            this.contRfTemp = Integer.parseInt(contRfTemp);
//            this.contTemperUnit = Integer.parseInt(contTemperUnit) > 0 ? 1 : 0;
//        }
        this.contIsMoreUnload = contIsMoreUnload;
        this.contIsOverWeight = contIsOverWeight;
        this.contIsCheck = contIsCheck;
        this.contIsCheckliest = contIsCheckliest;
        this.contIsReplace = contIsReplace;
        this.contOther = contOther;
        this.contRemark = contRemark;
        this.creator = creator;
    }

    @Id
    @Column(name = "CONT_ID", length = 19)
    @SequenceGenerator(name="SEQ_CONTAINER", sequenceName="SEQ_CONTAINER")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONTAINER")
    public Long getContId() {
        return contId;
    }

    public void setContId(Long contId) {
        this.contId = contId;
        addValidField("contId");
    }

    @Column(name = "CONT_ORDE_ID", length = 19)
    public Long getContOrdeId() {
        return contOrdeId;
    }

    public void setContOrdeId(Long contOrdeId) {
        this.contOrdeId = contOrdeId;
        addValidField("contOrdeId");
    }

    @Column(name = "CONT_OWNER", length = 64)
    public String getContOwner() {
        return contOwner;
    }

    public void setContOwner(String contOwner) {
        this.contOwner = contOwner;
        addValidField("contOwner");
    }

    @Column(name = "CONT_OWNER_CODE", length = 64)
    public String getContOwnerCode() {
        return contOwnerCode;
    }

    public void setContOwnerCode(String contOwnerCode) {
        this.contOwnerCode = contOwnerCode;
    }


    @Column(name = "CONT_CASE_NO", length = 64)
    public String getContCaseNo() {
        return contCaseNo;
    }

    public void setContCaseNo(String contCaseNo) {
        this.contCaseNo = contCaseNo;
        addValidField("contCaseNo");
    }


    @Column(name = "CONT_GBS_ID", length = 64)
    public String getContGbsId() {
        return contGbsId;
    }

    public void setContGbsId(String contGbsId) {
        this.contGbsId = contGbsId;
        addValidField("contGbsId");
    }


    @Column(name = "CONT_COUNT", length = 10)
    public Integer getContCount() {
        return contCount;
    }

    public void setContCount(Integer contCount) {
        this.contCount = contCount;
        addValidField("contCount");
    }

//    @Column(name = "CONT_IS_SEND", length = 1)
//    public Integer getContIsSend() {
//        return contIsSend;
//    }
//
//    public void setContIsSend(Integer contIsSend) {
//        this.contIsSend = contIsSend;
//        addValidField("contIsSend");
//    }


    @Column(name = "CONT_DRAYAGE_DEPOT", length = 128)
    public String getContDrayageDepot() {
        return contDrayageDepot;
    }

    public void setContDrayageDepot(String contDrayageDepot) {
        this.contDrayageDepot = contDrayageDepot;
        addValidField("contDrayageDepot");
    }

    @Column(name = "CONT_DRAYAGE_DEPOT_CODE", length = 32)
    public String getContDrayageDepotCode() {
        return contDrayageDepotCode;
    }

    public void setContDrayageDepotCode(String contDrayageDepotCode) {
        this.contDrayageDepotCode = contDrayageDepotCode;
        addValidField("contDrayageDepotCode");
    }

    @Column(name = "CONT_RETURN_DEPOT", length = 128)
    public String getContReturnDepot() {
        return contReturnDepot;
    }

    public void setContReturnDepot(String contReturnDepot) {
        this.contReturnDepot = contReturnDepot;
        addValidField("contReturnDepot");
    }

    @Column(name = "CONT_RETURN_DEPOT_CODE", length = 32)
    public String getContReturnDepotCode() {
        return contReturnDepotCode;
    }

    public void setContReturnDepotCode(String contReturnDepotCode) {
        this.contReturnDepotCode = contReturnDepotCode;
        addValidField("contReturnDepotCode");
    }

    @Column(name = "CONT_STATE", length = 2)
    public Integer getContState() {
        return contState;
    }

    public void setContState(Integer contState) {
        this.contState = contState;
        addValidField("contState");
    }

    @Column(name = "CONT_SEAL_NO", length = 64)
    public String getContSealNo() {
        return contSealNo;
    }

    public void setContSealNo(String contSealNo) {
        this.contSealNo = contSealNo;
        addValidField("contSealNo");
    }

    @Column(name = "CONT_TRADE_NAME", length = 64)
    public String getContTradeName() {
        return contTradeName;
    }

    public void setContTradeName(String contTradeName) {
        this.contTradeName = contTradeName;
        addValidField("contTradeName");
    }

    @Column(name = "CONT_TYPE", length = 64)
    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
        addValidField("contType");
    }


    @Column(name = "CONT_CARTON_SIZE", length = 64)
    public String getContCartonSize() {
        return contCartonSize;
    }

    public void setContCartonSize(String contCartonSize) {
        this.contCartonSize = contCartonSize;
        addValidField("contCartonSize");
    }

    @Column(name = "CONT_GROSS_WEIGHT", length = 14)
    public Double getContGrossWeight() {
        return contGrossWeight;
    }

    public void setContGrossWeight(Double contGrossWeight) {
        this.contGrossWeight = contGrossWeight;
    }

    @Column(name = "CONT_RF_TEMP", length = 10)
    public Integer getContRfTemp() {
        return contRfTemp;
    }

    public void setContRfTemp(Integer contRfTemp) {
        this.contRfTemp = contRfTemp;
        addValidField("contRfTemp");
    }

    @Column(name = "CONT_TEMPER_UNIT", length = 1)
    public Integer getContTemperUnit() {
        return contTemperUnit;
    }

    public void setContTemperUnit(Integer contTemperUnit) {
        this.contTemperUnit = contTemperUnit;
        addValidField("contTemperUnit");
    }


    @Column(name = "CONT_DYNAMO", length = 16)
    public String getContDynamo() {
        return contDynamo;
    }

    public void setContDynamo(String contDynamo) {
        this.contDynamo = contDynamo;
        addValidField("contDynamo");
    }

    @Column(name = "CONT_IS_MORE_UNLOAD", length = 16)
    public String getContIsMoreUnload() {
        return contIsMoreUnload;
    }

    public void setContIsMoreUnload(String contIsMoreUnload) {
        this.contIsMoreUnload = contIsMoreUnload;
        addValidField("contIsMoreUnload");
    }


    @Column(name = "CONT_IS_OVER_WEIGHT", length = 16)
    public String getContIsOverWeight() {
        return contIsOverWeight;
    }

    public void setContIsOverWeight(String contIsOverWeight) {
        this.contIsOverWeight = contIsOverWeight;
        addValidField("contIsOverWeight");
    }

    @Column(name = "CONT_IS_CHECK", length = 16)
    public String getContIsCheck() {
        return contIsCheck;
    }

    public void setContIsCheck(String contIsCheck) {
        this.contIsCheck = contIsCheck;
        addValidField("contIsCheck");
    }

    @Column(name = "CONT_IS_CHECKLIEST", length = 16)
    public String getContIsCheckliest() {
        return contIsCheckliest;
    }

    public void setContIsCheckliest(String contIsCheckliest) {
        this.contIsCheckliest = contIsCheckliest;
        addValidField("contIsCheckliest");
    }

    @Column(name = "CONT_IS_REPLACE", length = 16)
    public String getContIsReplace() {
        return contIsReplace;
    }

    public void setContIsReplace(String contIsReplace) {
        this.contIsReplace = contIsReplace;
        addValidField("contIsReplace");
    }

    @Column(name = "CONT_COST", precision = 15, scale = 2)
    public Double getContCost() {
        return contCost;
    }

    public void setContCost(Double contCost) {
        this.contCost = contCost;
        addValidField("contCost");
    }

    @Column(name = "CONT_OTHER", length = 256)
    public String getContOther() {
        return contOther;
    }

    public void setContOther(String contOther) {
        this.contOther = contOther;
        addValidField("contOther");
    }

    @Column(name = "CONT_REMARK", length = 256)
    public String getContRemark() {
        return contRemark;
    }

    public void setContRemark(String contRemark) {
        this.contRemark = contRemark;
        addValidField("contRemark");
    }

    @Column(name = "CREATOR", length = 32)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
        addValidField("creator");
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        addValidField("createTime");
    }

    @Column(name = "MODIFIER", length = 32)
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
        addValidField("modifier");
    }

    @Column(name = "MODIFY_TIME")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        addValidField("modifyTime");
    }

    @Column(name = "REC_VER", length = 19)
    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
        addValidField("recVer");
    }

    @Column(name = "REC_STATUS")
    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
        addValidField("recStatus");
    }

    public void checkState(Logger logger, ContainerStateType... stateType) {
        boolean stateOk = false;
        if (null == stateType) {
            return;
        }
        for (ContainerStateType s : stateType) {
            stateOk = stateOk || this.getContState().intValue() == s.getValue();
            if (stateOk) {
                return;
            }
        }
        throw new ContainerStateException(ContainerStateType.valueOf(this.contState), this.contId ,logger);
    }

    public void checkDelete(Logger logger) {
        if (DomainStatus.Delete.getValue() == recStatus.intValue()) {//todo: 其他地方需要陆陆续续判断是否被删除
            throw new RecordHasDeleteException(DomainType.Order, this.contId, logger);
        }
    }

//    public void checkBelongCreator(Long userId, Logger logger) {
//        if (userId.longValue() != Long.parseLong(this.creator)) {
//            throw new DomainNotBelongException(DomainType.Container, this.contId, this.creator, userId, logger);
//        }
//    }
}
