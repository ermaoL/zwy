package com.sinotrans.transport.dto.vo;

/**
 * Created by emi on 2016/6/13.
 */
public class UserSearchResponseVo {

    private String index;

    private String userId;

    private String userName;

    private String userTel;

    private String userCompany;

    public UserSearchResponseVo(int index, Long userId, String userName, String userTel, String userCompany) {
        this();
        this.index = String.valueOf(index);
        this.userId = String.valueOf(userId);
        this.userName = userName;
        this.userTel = userTel;
        this.userCompany = userCompany;
    }

    public UserSearchResponseVo() {
        super();
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }
}
