package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.*;
import com.sinotrans.transport.common.exception.*;
import com.sinotrans.transport.dto.vo.ContainerImportReqVo;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by emi on 2016/5/11.
 */
@Entity
@Table(name = "ORDERD")
public class Order extends BaseModel implements OperationLog {

    private Long ordeId;

    private String ordeBillNo;//提单号                         export,import

    private String ordeSystemNo;//系统订单号                     export,import       去掉

    private String ordeGbsDocCode;//gbs订单中的DOC_CODE（主键）

    private Integer ordeState;//单据状态                        export,import

    private Integer ordeType;//订单类型                         export,import

    private Integer ordeTranCompany;//运输公司 登录用户所属运输公司  export,import      //裕雄 裕国

    private String ordeWharf;//码头                           export,import
    private String ordeWharfCode;//码头代码                     export,import

    private String ordeShipName;//船名                        export,import
    private String ordeShipNameCode;//船名代码                  export,import

    private String ordeSailing;//船次,航次                      export,import
    private String ordeSailingCode;//船次,航次                  export,import

    private String ordeShipAgency;//船代                          export,import

    private String ordeShippingCode;//货代代码                   export

    private String ordeBookingNumber;//电子订舱号                export

    private Date ordeSailingDate;//开行日期                      export

    private Date ordeArrivalDate;//抵港日期                             import

    private String ordeFreeUseDay;//免费使用日期                  export,import       去掉

    private String ordePort;//起运港                               export              出口去掉
    private String ordePortCode;//起运港                           export              出口去掉

    private String ordeEnterPort;//中转港                          export
    private String ordeEnterPortCode;//中转港                      export

    private String ordeDestinationPort;//目的港                    export
    private String ordeDestinationPortCode;//目的港                export

    private String ordeOwner;//箱主                               export,import
    private String ordeOwnerCode;//箱主代码                        export,import

    private String ordeGoodOwner;//货主  属于注册公司，但跟当前登录用户的无关 export,import
    private String ordeGoodOwnerCode;//货主code                    export,import

    private Integer ordeTotalCase;//总箱量，数据库箱子数量        export,import

    private Integer ordeSurplusTank;//剩余柜量                      export,import       去掉

    private String ordeCheckContainer;//是否验柜                    export              去掉

    private String ordeCompanyCode;//下单公司code，登录用户所属公司 export,import
    private String ordeCompanyName;//下单公司                       export,import

    private String ordeVerifier;//审核人                           export,import

    private String ordeVerifierContent;//审核内容                   export,import

    private String ordeRemark;//备注                                 export,import

    private String ordeConnects;//订单联系人                         export,import

    private String ordeConnectEmail;//联系人邮箱                     export,import

    private String ordeConnectPhone;//联系人手机                     export,import

    private String creator;//创建人

    private Date createTime;//创建时间

    private String modifier;//最后修改人

    private Date modifyTime;//最后修改时间

    private Long recVer;//数据行版本号

    private Integer recStatus;//数据行状态(0:正常/1:删除)

    public Order() {
        this.recVer = 0L;
        this.recStatus = 0;
//        this.ordeIsSend = 0;
        this.ordeState = OrderStateType.Create.getValue();
        this.ordeTotalCase = 0;
    }

    //进口
    public Order(String ordeBillNo, String ordeSystemNo, Integer ordeTranCompany, String ordeWharf, String ordeWharfCode, String ordeShipName, String ordeShipNameCode, String ordeSailing, String ordeSailingCode, String ordeOwnerCode, String ordeOwner,String ordeGoodOwnerCode, String ordeGoodOwner,
                 String ordeShipAgency, String ordeArrivalDate, String ordeCompanyCode, String ordeCompanyName, String ordeTotalCase, String ordeRemark, String ordeConnects, String ordeConnectEmail, String ordeConnectPhone, Long creator, SimpleDateFormat sdf) throws ParseException {
        this();
        this.ordeType = OrderType.Import.getValue();
        this.ordeBillNo = ordeBillNo;
        this.ordeSystemNo = ordeSystemNo;
        this.ordeTranCompany = ordeTranCompany;
        this.ordeWharf = ordeWharf;
        this.ordeWharfCode = ordeWharfCode;
        this.ordeShipName = ordeShipName;
        this.ordeShipNameCode = ordeShipNameCode;
        this.ordeSailing = ordeSailing;
        this.ordeSailingCode = ordeSailingCode;
        this.ordeOwnerCode = ordeOwnerCode;
        this.ordeOwner = ordeOwner;
        this.ordeGoodOwner = ordeGoodOwner;
        this.ordeGoodOwnerCode = ordeGoodOwnerCode;
        this.ordeShipAgency = ordeShipAgency;
        this.ordeArrivalDate = StringUtils.isEmpty(ordeArrivalDate) ? null : sdf.parse(ordeArrivalDate);
//        this.ordeFreeUseDay = ordeFreeUseDay;
        this.ordeTotalCase = StringUtils.isEmpty(ordeTotalCase) ? 0 : Integer.parseInt(ordeTotalCase);
//        this.ordeSurplusTank = Integer.parseInt(ordeSurplusTank);
        this.ordeCompanyCode = ordeCompanyCode;
        this.ordeCompanyName = ordeCompanyName;
        this.ordeRemark = ordeRemark;
        this.ordeConnects = ordeConnects;
        this.ordeConnectEmail = ordeConnectEmail;
        this.ordeConnectPhone = ordeConnectPhone;
        this.creator = String.valueOf(creator);
    }

    //出口
    public Order(String ordeBillNo, String ordeSystemNo, Integer ordeTranCompany, String ordeWharf, String ordeWharfCode, String ordeShipName, String ordeShipNameCode, String ordeSailing, String ordeSailingCode, String ordeShipAgency, String ordeSailingDate,
                 String ordeEnterPort, String ordeEnterPortCode, String ordeDestinationPort, String ordeDestinationPortCode, String ordeShippingCode, String ordeBookingNumber, String ordeTotalCase, String ordeOwner, String ordeOwnerCode, String ordeGoodOwner,
                 String ordeGoodOwnerCode, String ordeCompanyCode, String ordeCompanyName, String ordeRemark, String ordeConnects, String ordeConnectEmail, String ordeConnectPhone, Long creator, SimpleDateFormat sdf) throws ParseException {
        this();
        this.ordeType = OrderType.Export.getValue();
        this.ordeBillNo = ordeBillNo;
        this.ordeSystemNo = ordeSystemNo;
        this.ordeTranCompany = ordeTranCompany;
        this.ordeWharf = ordeWharf;
        this.ordeWharfCode = ordeWharfCode;
        this.ordeShipName = ordeShipName;
        this.ordeShipNameCode = ordeShipNameCode;
        this.ordeSailing = ordeSailing;
        this.ordeSailingCode = ordeSailingCode;
        this.ordeShipAgency = ordeShipAgency;
        this.ordeSailingDate = StringUtils.isEmpty(ordeSailingDate) ? null : sdf.parse(ordeSailingDate);
//        this.ordePort = ordePort;
//        this.ordePortCode = ordePortCode;
        this.ordeEnterPort = ordeEnterPort;
        this.ordeEnterPortCode = ordeEnterPortCode;
        this.ordeDestinationPort = ordeDestinationPort;
        this.ordeDestinationPortCode = ordeDestinationPortCode;
        this.ordeShippingCode = ordeShippingCode;
        this.ordeBookingNumber = ordeBookingNumber;
        this.ordeTotalCase = StringUtils.isEmpty(ordeTotalCase) ? 0 : Integer.parseInt(ordeTotalCase);
        this.ordeOwner = ordeOwner;
        this.ordeOwnerCode = ordeOwnerCode;
        this.ordeGoodOwner = ordeGoodOwner;
        this.ordeGoodOwnerCode = ordeGoodOwnerCode;
//        this.ordeFreeUseDay = ordeFreeUseDay;
//        this.ordeCheckContainer = ordeCheckContainer;
        this.ordeCompanyCode = ordeCompanyCode;
        this.ordeCompanyName = ordeCompanyName;
        this.ordeRemark = ordeRemark;
        this.ordeRemark = ordeRemark;
        this.ordeConnects = ordeConnects;
        this.ordeConnectEmail = ordeConnectEmail;
        this.ordeConnectPhone = ordeConnectPhone;
        this.creator = String.valueOf(creator);
    }


    @Id
    @Column(name = "ORDE_ID", length = 19)
    @SequenceGenerator(name="SEQ_ORDER", sequenceName="SEQ_ORDER")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ORDER")
    public Long getOrdeId() {
        return ordeId;
    }

    public void setOrdeId(Long ordeId) {
        this.ordeId = ordeId;
        addValidField("ordeId");
    }

    @Column(name = "ORDE_BILL_NO", length = 64)
    public String getOrdeBillNo() {
        return ordeBillNo;
    }

    public void setOrdeBillNo(String ordeBillNo) {
        this.ordeBillNo = ordeBillNo;
        addValidField("ordeBillNo");
    }

    @Column(name = "ORDE_SYSTEM_NO", length = 64)
    public String getOrdeSystemNo() {
        return ordeSystemNo;
    }

    public void setOrdeSystemNo(String ordeSystemNo) {
        this.ordeSystemNo = ordeSystemNo;
        addValidField("ordeSystemNo");
    }

    @Column(name = "ORDE_GBS_DOC_CODE", length = 64)
    public String getOrdeGbsDocCode() {
        return ordeGbsDocCode;
    }

    public void setOrdeGbsDocCode(String ordeGbsDocCode) {
        this.ordeGbsDocCode = ordeGbsDocCode;
        addValidField("ordeGbsDocCode");
    }

    @Column(name = "ORDE_STATE", length = 2)
    public Integer getOrdeState() {
        return ordeState;
    }

    public void setOrdeState(Integer ordeState) {
        this.ordeState = ordeState;
        addValidField("ordeState");
    }

    @Column(name = "ORDE_TYPE", length = 2)
    public Integer getOrdeType() {
        return ordeType;
    }

    public void setOrdeType(Integer ordeType) {
        this.ordeType = ordeType;
        addValidField("ordeType");
    }

//    @Column(name = "ORDE_IS_SEND", length = 1)
//    public Integer getOrdeIsSend() {
//        return ordeIsSend;
//    }
//
//    public void setOrdeIsSend(Integer ordeIsSend) {
//        this.ordeIsSend = ordeIsSend;
//        addValidField("ordeIsSend");
//    }

    @Column(name = "ORDE_TRAN_COMPANY", length = 2)
    public Integer getOrdeTranCompany() {
        return ordeTranCompany;
    }

    public void setOrdeTranCompany(Integer ordeTranCompany) {
        this.ordeTranCompany = ordeTranCompany;
    }


    @Column(name = "ORDE_WHARF", length = 64)
    public String getOrdeWharf() {
        return ordeWharf;
    }

    public void setOrdeWharf(String ordeWharf) {
        this.ordeWharf = ordeWharf;
        addValidField("ordeWharf");
    }

    @Column(name = "ORDE_WHARF_CODE", length = 64)
    public String getOrdeWharfCode() {
        return ordeWharfCode;
    }

    public void setOrdeWharfCode(String ordeWharfCode) {
        this.ordeWharfCode = ordeWharfCode;
        addValidField("ordeWharfCode");
    }

    @Column(name = "ORDE_SHIP_NAME", length = 64)
    public String getOrdeShipName() {
        return ordeShipName;
    }

    public void setOrdeShipName(String ordeShipName) {
        this.ordeShipName = ordeShipName;
        addValidField("ordeShipName");
    }

    @Column(name = "ORDE_SHIP_NAME_CODE", length = 64)
    public String getOrdeShipNameCode() {
        return ordeShipNameCode;
    }

    public void setOrdeShipNameCode(String ordeShipNameCode) {
        this.ordeShipNameCode = ordeShipNameCode;
        addValidField("ordeShipNameCode");
    }

    @Column(name = "ORDE_SAILING", length = 64)
    public String getOrdeSailing() {
        return ordeSailing;
    }

    public void setOrdeSailing(String ordeSailing) {
        this.ordeSailing = ordeSailing;
        addValidField("ordeSailing");
    }

    @Column(name = "ORDE_SAILING_CODE", length = 64)
    public String getOrdeSailingCode() {
        return ordeSailingCode;
    }

    public void setOrdeSailingCode(String ordeSailingCode) {
        this.ordeSailingCode = ordeSailingCode;
        addValidField("ordeSailingCode");
    }

    @Column(name = "ORDE_SHIP_AGENCY", length = 64)
    public String getOrdeShipAgency() {
        return ordeShipAgency;
    }

    public void setOrdeShipAgency(String ordeShipAgency) {
        this.ordeShipAgency = ordeShipAgency;
        addValidField("ordeShipAgency");
    }

    @Column(name = "ORDE_SHIPPING_CODE", length = 64)
    public String getOrdeShippingCode() {
        return ordeShippingCode;
    }

    public void setOrdeShippingCode(String ordeShippingCode) {
        this.ordeShippingCode = ordeShippingCode;
        addValidField("ordeShippingCode");
    }

    @Column(name = "ORDE_BOOKING_NUMBER", length = 64)
    public String getOrdeBookingNumber() {
        return ordeBookingNumber;
    }

    public void setOrdeBookingNumber(String ordeBookingNumber) {
        this.ordeBookingNumber = ordeBookingNumber;
        addValidField("ordeBookingNumber");
    }

    @Column(name = "ORDE_SAILING_DATE")
    public Date getOrdeSailingDate() {
        return ordeSailingDate;
    }

    public void setOrdeSailingDate(Date ordeSailingDate) {
        this.ordeSailingDate = ordeSailingDate;
        addValidField("ordeSailingDate");
    }

    @Column(name = "ORDE_ARRIVAL_DATE")
    public Date getOrdeArrivalDate() {
        return ordeArrivalDate;
    }

    public void setOrdeArrivalDate(Date ordeArrivalDate) {
        this.ordeArrivalDate = ordeArrivalDate;
        addValidField("ordeArrivalDate");
    }

    @Column(name = "ORDE_FREE_USE_DAY", length = 32)
    public String getOrdeFreeUseDay() {
        return ordeFreeUseDay;
    }

    public void setOrdeFreeUseDay(String ordeFreeUseDay) {
        this.ordeFreeUseDay = ordeFreeUseDay;
        addValidField("ordeFreeUseDay");
    }

    @Column(name = "ORDE_PORT", length = 64)
    public String getOrdePort() {
        return ordePort;
    }

    public void setOrdePort(String ordePort) {
        this.ordePort = ordePort;
        addValidField("ordePort");
    }

    @Column(name = "ORDE_PORT_CODE", length = 64)
    public String getOrdePortCode() {
        return ordePortCode;
    }

    public void setOrdePortCode(String ordePortCode) {
        this.ordePortCode = ordePortCode;
        addValidField("ordePortCode");
    }

    @Column(name = "ORDE_ENTER_PORT", length = 64)
    public String getOrdeEnterPort() {
        return ordeEnterPort;
    }

    public void setOrdeEnterPort(String ordeEnterPort) {
        this.ordeEnterPort = ordeEnterPort;
        addValidField("ordeEnterPort");
    }

    @Column(name = "ORDE_ENTER_PORT_CODE", length = 64)
    public String getOrdeEnterPortCode() {
        return ordeEnterPortCode;
    }

    public void setOrdeEnterPortCode(String ordeEnterPortCode) {
        this.ordeEnterPortCode = ordeEnterPortCode;
        addValidField("ordeEnterPortCode");
    }

    @Column(name = "ORDE_DESTINATION_PORT", length = 64)
    public String getOrdeDestinationPort() {
        return ordeDestinationPort;
    }

    public void setOrdeDestinationPort(String ordeDestinationPort) {
        this.ordeDestinationPort = ordeDestinationPort;
        addValidField("ordeDestinationPort");
    }

    @Column(name = "ORDE_DESTINATION_PORT_CODE", length = 64)
    public String getOrdeDestinationPortCode() {
        return ordeDestinationPortCode;
    }

    public void setOrdeDestinationPortCode(String ordeDestinationPortCode) {
        this.ordeDestinationPortCode = ordeDestinationPortCode;
        addValidField("ordeDestinationPortCode");
    }

    @Column(name = "ORDE_OWNER", length = 64)
    public String getOrdeOwner() {
        return ordeOwner;
    }

    public void setOrdeOwner(String ordeOwner) {
        this.ordeOwner = ordeOwner;
        addValidField("ordeOwner");
    }

    @Column(name = "ORDE_OWNER_CODE", length = 64)
    public String getOrdeOwnerCode() {
        return ordeOwnerCode;
    }

    public void setOrdeOwnerCode(String ordeOwnerCode) {
        this.ordeOwnerCode = ordeOwnerCode;
        addValidField("ordeOwnerCode");
    }

    @Column(name = "ORDE_GOOD_OWNER", length = 128)
    public String getOrdeGoodOwner() {
        return ordeGoodOwner;
    }

    public void setOrdeGoodOwner(String ordeGoodOwner) {
        this.ordeGoodOwner = ordeGoodOwner;
        addValidField("ordeGoodOwner");
    }

    @Column(name = "ORDE_GOOD_OWNER_CODE", length = 32)
    public String getOrdeGoodOwnerCode() {
        return ordeGoodOwnerCode;
    }

    public void setOrdeGoodOwnerCode(String ordeGoodOwnerCode) {
        this.ordeGoodOwnerCode = ordeGoodOwnerCode;
        addValidField("ordeGoodOwnerCode");
    }

    @Column(name = "ORDE_TOTAL_CASE", length = 10)
    public Integer getOrdeTotalCase() {
        return ordeTotalCase;
    }

    public void setOrdeTotalCase(Integer ordeTotalCase) {
        this.ordeTotalCase = ordeTotalCase;
        addValidField("ordeTotalCase");
    }

    @Column(name = "ORDE_SURPLUS_TANK", length = 10)
    public Integer getOrdeSurplusTank() {
        return ordeSurplusTank;
    }

    public void setOrdeSurplusTank(Integer ordeSurplusTank) {
        this.ordeSurplusTank = ordeSurplusTank;
        addValidField("ordeSurplusTank");
    }

    @Column(name = "ORDE_CHECK_CONTAINER", length = 16)
    public String getOrdeCheckContainer() {
        return ordeCheckContainer;
    }

    public void setOrdeCheckContainer(String ordeCheckContainer) {
        this.ordeCheckContainer = ordeCheckContainer;
        addValidField("ordeCheckContainer");
    }

    @Column(name = "ORDE_COMPANY_CODE", length = 64)
    public String getOrdeCompanyCode() {
        return ordeCompanyCode;
    }

    public void setOrdeCompanyCode(String ordeCompanyCode) {
        this.ordeCompanyCode = ordeCompanyCode;
        addValidField("ordeCompanyCode");
    }


    @Column(name = "ORDE_COMPANY_NAME", length = 64)
    public String getOrdeCompanyName() {
        return ordeCompanyName;
    }

    public void setOrdeCompanyName(String ordeCompanyName) {
        this.ordeCompanyName = ordeCompanyName;
        addValidField("ordeCompanyName");
    }

    @Column(name = "ORDE_VERIFIER", length = 64)
    public String getOrdeVerifier() {
        return ordeVerifier;
    }

    public void setOrdeVerifier(String ordeVerifier) {
        this.ordeVerifier = ordeVerifier;
        addValidField("ordeVerifier");
    }

    @Column(name = "ORDE_VERIFIER_CONTENT", length = 256)
    public String getOrdeVerifierContent() {
        return ordeVerifierContent;
    }

    public void setOrdeVerifierContent(String ordeVerifierContent) {
        this.ordeVerifierContent = ordeVerifierContent;
        addValidField("ordeVerifierContent");
    }

    @Column(name = "ORDE_REMARK", length = 4000)
    public String getOrdeRemark() {
        return ordeRemark;
    }

    public void setOrdeRemark(String ordeRemark) {
        this.ordeRemark = ordeRemark;
        addValidField("ordeRemark");
    }

    @Column(name = "ORDE_CONNECTS", length = 64)
    public String getOrdeConnects() {
        return ordeConnects;
    }

    public void setOrdeConnects(String ordeConnects) {
        this.ordeConnects = ordeConnects;
        addValidField("ordeConnects");
    }

    @Column(name = "ORDE_CONNECT_EMAIL", length = 64)
    public String getOrdeConnectEmail() {
        return ordeConnectEmail;
    }

    public void setOrdeConnectEmail(String ordeConnectEmail) {
        this.ordeConnectEmail = ordeConnectEmail;
        addValidField("ordeConnectEmail");
    }

    @Column(name = "ORDE_CONNECT_PHONE", length = 14)
    public String getOrdeConnectPhone() {
        return ordeConnectPhone;
    }

    public void setOrdeConnectPhone(String ordeConnectPhone) {
        this.ordeConnectPhone = ordeConnectPhone;
        addValidField("ordeConnectPhone");
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

    public void checkState(Logger logger, OrderStateType... stateType) {
        boolean stateOk = false;
        if (null == stateType) {
            return;
        }
        for (OrderStateType s : stateType) {
            stateOk = stateOk || this.ordeState.intValue() == s.getValue();
            if (stateOk) {
                return;
            }
        }
        throw new OrderStateException(OrderStateType.valueOf(this.ordeState), this.ordeId ,logger);
    }

    public void checkDelete(Logger logger) {
        if (DomainStatus.Delete.getValue() == recStatus.intValue()) {//todo: 其他地方需要陆陆续续判断是否被删除
            throw new RecordHasDeleteException(DomainType.Order, this.ordeId, logger);
        }
    }

    public void checkBelongCreator(Long userId, Logger logger) {
        if (userId.longValue() != Long.parseLong(this.creator)) {
            throw new DomainNotBelongException(DomainType.Order, this.ordeId, this.creator, userId, logger);
        }
    }

    public void checkBelongCompany(CompanyType companyType, Logger logger) {
        if (CompanyType.AdminAll != companyType) {
            if (companyType.getValue() != this.getOrdeTranCompany().intValue()) {
                throw new DomainNotBelongCompanyException(DomainType.Order, this.ordeId, CompanyType.descOf(this.getOrdeTranCompany()), companyType.getDesc(), logger);
            }
        }
    }

    public String verifySuccess(CompanyType companyType, String adminCode, String adminName, String orderGbsDocCode) {

        setOrdeState( OrderStateType.Running.getValue());
        if (!StringUtils.isEmpty(this.ordeVerifierContent)) {
            setOrdeVerifierContent("");
        }
        setOrdeVerifier(CompanyType.AdminAll == companyType ? companyType.getCode() : companyType.getCode() + ": " + adminName);
        setModifier(adminCode);
        setOrdeGbsDocCode(orderGbsDocCode);
        return ordeVerifier;
    }

    public String verifyFailed(CompanyType companyType, String adminCode, String adminName, String failedReason) {

        setOrdeState(OrderStateType.Failed.getValue());
        setOrdeVerifierContent(failedReason);
        setOrdeVerifier(CompanyType.AdminAll == companyType ? companyType.getCode() : companyType.getCode() + ": " + adminName);
        setModifier(adminCode);
        return ordeVerifier;
    }

//    public boolean checkOrderTotalCase(List containVoList) {
//
//        if (null == containVoList || 0 == containVoList.size()) {
//            if (0 != this.ordeTotalCase.intValue()) {
//                setOrdeTotalCase(0);
//                return true;
//            }
//        } else {
//            if (this.ordeTotalCase.intValue() != containVoList.size()) {
//                setOrdeTotalCase(containVoList.size());
//                return true;
//            }
//        }
//        return false;
//    }
}
