package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.OrderStateType;
import com.sinotrans.transport.common.enums.UserStateType;
import com.sinotrans.transport.common.exception.CommonException;
import com.sinotrans.transport.common.exception.OrderStateException;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/5/10.
 */
@Entity
@Table(name = "USERS")
public class User extends BaseModel implements OperationLog {

    private Long userId;

    private String userName;

    private Integer userState;//1:审核中 2:未通过 3:通过  4:启用  5:停用

    private String userTel;

    private String userPwd;

    private String userCompanyCode;//公司Id

    private String userCompany;//公司

    private String userVerifier;//审核人

    private String userMark;//审核不通过的理由

    private String userConnects;

    private String userConnectEmail;

    private String userConnectPhone;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Long recVer;//数据行版本号

    private Integer recStatus;// 0:正常   1：被软删除

    public User() {
        super();
        this.userState = 1;
        this.createTime = new Date();
        this.modifyTime = this.createTime;
        this.recVer = 0L;
        this.recStatus = 0;
    }

    public User(String userName, String userPwd, String userTel, String userCompany) {
        this();
        this.userName = userName;
        this.userPwd = userPwd;
        this.userTel = userTel;
        this.userCompany = userCompany;
    }


    @Column(name = "USER_ID", length = 19)
    @SequenceGenerator(name="SEQ_USER", sequenceName="SEQ_USER",allocationSize = 1)
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USER")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        addValidField("userId");
    }

    @Column(name = "USER_NAME",length = 32)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        addValidField("userName");
    }

    @Column(name = "USER_STATE",length = 2)
    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
        addValidField("userState");
    }

    @Column(name = "USER_TEL",length = 11)
    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
        addValidField("userTel");
    }

    @Column(name = "USER_PWD", length = 64)
    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
        addValidField("userPwd");
    }


    @Column(name = "USER_COMPANY_CODE", length = 64)
    public String getUserCompanyCode() {
        return userCompanyCode;
    }

    public void setUserCompanyCode(String userCompanyCode) {
        this.userCompanyCode = userCompanyCode;
        addValidField("userCompanyCode");
    }


    @Column(name = "USER_COMPANY", length = 128)
    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
        addValidField("userCompany");
    }


    @Column(name = "USER_VERIFIER", length = 32)
    public String getUserVerifier() {
        return userVerifier;
    }

    public void setUserVerifier(String userVerifier) {
        this.userVerifier = userVerifier;
        addValidField("userVerifier");
    }


    @Column(name = "USER_MARK", length = 256)
    public String getUserMark() {
        return userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark;
        addValidField("userMark");
    }

    @Column(name = "USER_CONNECTS", length = 64)
    public String getUserConnects() {
        return userConnects;
    }

    public void setUserConnects(String userConnects) {
        this.userConnects = userConnects;
        addValidField("userConnects");
    }

    @Column(name = "USER_CONNECT_EMAIL", length = 64)
    public String getUserConnectEmail() {
        return userConnectEmail;
    }

    public void setUserConnectEmail(String userConnectEmail) {
        this.userConnectEmail = userConnectEmail;
        addValidField("userConnectEmail");
    }

    @Column(name = "USER_CONNECT_PHONE", length = 14)
    public String getUserConnectPhone() {
        return userConnectPhone;
    }

    public void setUserConnectPhone(String userConnectPhone) {
        this.userConnectPhone = userConnectPhone;
        addValidField("userConnectPhone");
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

    @Column(name = "REC_STATUS", length = 1)
    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
        addValidField("recStatus");
    }

    public void checkState(Logger logger, UserStateType... stateType) {
        boolean stateOk = false;
        if (null == stateType) {
            return;
        }
        for (UserStateType s : stateType) {
            stateOk = stateOk || this.userState.intValue() == s.getValue();
            if (stateOk) {
                return;
            }
        }
        throw new CommonException(ErrorCode.USER_STATE_ERROR, "用户状态为[" + UserStateType.descOf(userState) + "]，不能审核", "用户[" + this.userId + "]状态为[" + UserStateType.descOf(userState) + "]，不能审核", logger);
    }


    public void verifyUserPass(String adminCode, String belongCompanyCode, String belongCompany) {
        setUserVerifier(adminCode);
        setUserCompanyCode(belongCompanyCode);
        setUserCompany(belongCompany);
        setUserState(UserStateType.Run.getValue());
        setModifier(adminCode);
    }

    public void verifyUserFail(String adminCode, String failedReason) {
        setUserVerifier(adminCode);
        setUserState(UserStateType.Failed.getValue());
        setUserVerifier(failedReason);
        setModifier(adminCode);
    }
}
