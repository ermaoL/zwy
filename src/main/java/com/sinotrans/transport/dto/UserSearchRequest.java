package com.sinotrans.transport.dto;

import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.common.enums.DomainStatus;
import com.sinotrans.transport.common.enums.UserStateType;
import com.sinotrans.transport.dto.common.CommonListRequest;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emi on 2016/6/13.
 */
public class UserSearchRequest extends CommonListRequest {

    private String userName;

    private String userTel;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }


    @Override
    public List fetchFilter(Long userId, CompanyType companyType) throws ParseException {
        return null;
    }

    public List fetchFilter() {
        List filter = new ArrayList();
        if (!StringUtils.isEmpty(this.userName)) {
            filter.add(new FilterCondition("userName", this.userName, "likeAnywhere"));
        }
        if (!StringUtils.isEmpty(this.userTel)) {
            filter.add(new FilterCondition("userTel", this.userTel, "likeAnywhere"));
        }
        filter.add(new FilterCondition("userState", UserStateType.Checking.getValue(), "="));
        filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));
        return filter;
    }


}
