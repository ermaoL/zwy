package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.common.enums.DomainStatus;
import com.sinotrans.transport.common.enums.DomainType;
import com.sinotrans.transport.common.enums.UserStateType;
import com.sinotrans.transport.common.cache.SingletonCache;
import com.sinotrans.transport.common.exception.CommonException;
import com.sinotrans.transport.common.exception.RecordNotFoundException;
import com.sinotrans.transport.common.util.MD5Utils;
import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.model.Customer;
import com.sinotrans.transport.model.User;
import com.sinotrans.transport.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emi on 2016/5/10.
 */

@Service
public class UserServiceImpl extends MybatisManagerImpl implements UserService{

    Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Override
    public UserRegisterResponse register(UserRegisterRequest request, String ipAddress) {

        if (!request.equalPass()) {
            return new UserRegisterResponse(ErrorCode.USER_PASS_NOT_EQUAL, "两次密码不一致");
        }
        int countByName = myBatisDao.getRowCountByCondition(User.class, this.fetchUserRegisterFiler(request.getUserName()));
        if (countByName >= 1) {
            return new UserRegisterResponse(ErrorCode.USER_NAME_EXIST, "用户名已被占用");
        }
        int countPass = myBatisDao.getRowCountByCondition(User.class, this.fetchUserRegisterPhoneFiler(request.getUserTel()));
        if (countPass >= 1) {
            return new UserRegisterResponse(ErrorCode.USER_TEL_EXIST,"手机号码已被注册");
        }
        String md5pass = MD5Utils.string2MD5(request.getUserPass());
        User user = new User(request.getUserName(),md5pass, request.getUserTel(), request.getUserCompany());
        myBatisDao.save(user);

        String md5Token = SingletonCache.putUpdateData(user.getUserId(), user.getUserName(), ipAddress);//todo: userId
        return new UserRegisterResponse(user.getUserName(), md5Token);
    }


    @Override
    public UserLoginResponse login(String userName, String userPass, String ipAddress) {

        String md5pass = MD5Utils.string2MD5(userPass);
//        List<Map<String,Object>> userList = myBatisDao.queryByCondition("SearchUserByLogin", this.fetchUserLoginCondition(userName, md5pass, false), null, null, null);
        List<User> userList = myBatisDao.findByCondition(User.class, null, null, this.fetchUserLoginFiler(userName, md5pass, true));
        if (null == userList || 0 == userList.size()) {
            int countUser = myBatisDao.getRowCountByCondition(User.class, this.fetchUserNameFiler(userName ,false));
            if (0 == countUser) {
                return new UserLoginResponse(ErrorCode.USER_NAME_NOT_EXIST, "用户名不存在");
            } else {
                User user = myBatisDao.findByCondition(User.class, null, null, fetchUserNameFiler(userName, false)).get(0);
                if (!md5pass.equals(user.getUserPwd())) {
                    return new UserLoginResponse(ErrorCode.USER_PASS_ERROR, "用户密码错误");
                } else {
                    return new UserLoginResponse(ErrorCode.USER_PERMISSION_DENIED, String.format("用户状态为%s，不允许登录操作", UserStateType.descOf(user.getUserState())));
                }
            }
        } else if (1 == userList.size()) {
            User user = userList.get(0);
            String md5Token = SingletonCache.putUpdateData(user.getUserId(), userName, ipAddress);
            List<Customer> customerList = myBatisDao.findByCondition(Customer.class, null, null, this.fetchCustomerUserFilter(user.getUserId(), false));
            if (null == customerList || 1 != customerList.size()) {
                throw new CommonException(ErrorCode.CUSTOMER_SIZE_ERROR, "客户不存在,,或存在多个", "用户[" + user.getUserId() + "]的客户数量不=1", logger);
            }
            return new UserLoginResponse(userName, md5Token, customerList.get(0).getCustType() + "", user.getUserCompanyCode(), user.getUserCompany(), user.getUserConnects(), user.getUserConnectEmail(), user.getUserConnectPhone());
        } else {
            throw new CommonException(ErrorCode.USER_NAME_NOT_UNIQUE, "用户名不唯一，请联系管理员", "用户名[" + userName + "]在数据库中不唯一[" + userList.size() + "]", logger);
        }
    }

    @Override
    public UserAlterPassResponse alterPass(UserAlterPassRequest alterPassRequest, long userId) {

        User user = myBatisDao.get(User.class, userId);
        if (null == user) {
            throw new RecordNotFoundException(DomainType.User, userId, logger);
        }
        if (UserStateType.Stop.getValue() == user.getUserState()) {
            return new UserAlterPassResponse(ErrorCode.USER_PERMISSION_DENIED, String.format("用户状态为%s，不允许修改密码操作", UserStateType.descOf(user.getUserState())));
        }
        String md5pass = MD5Utils.string2MD5(alterPassRequest.getOldPass());
        if (md5pass.equals(user.getUserPwd())) {
            user.setUserPwd(MD5Utils.string2MD5(alterPassRequest.getNewPass()));
            myBatisDao.update(user);
            return new UserAlterPassResponse();
        } else {
            return new UserAlterPassResponse(ErrorCode.USER_PASS_NOT_EQUAL, "用户旧密码错误");
        }
    }

    @Override
    public boolean bind(Long userId, int companyType) {
        User user = myBatisDao.get(User.class, userId);
        if (null == user) {
            return false;
        }
        Customer customer = null;
        List<Customer> customerList = myBatisDao.findByCondition(Customer.class, null, null, this.fetchCustomerUserFilter(userId, false));
        if (null == customerList || 0 == customerList.size()) {
            if (1 == companyType) {
                customer = new Customer(userId, CompanyType.YuXiong);
            } else {
                customer = new Customer(userId, CompanyType.YuGuo);
            }
            myBatisDao.save(customer);
            return true;
        }
        return false;
    }

    private List<FilterCondition> fetchCustomerUserFilter(Long userId, boolean checkStatus) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("custUserId", userId, "="));//likeAnywhere
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));
        }
        return filter;
    }

    private List<FilterCondition> fetchUserLoginFiler(String userName, String md5pass, boolean ifCheck) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("userName", userName, "="));//likeAnywhere
        filter.add(new FilterCondition("userPwd", md5pass, "="));//likeAnywhere
        if (ifCheck) {
            filter.add(new FilterCondition("userState", UserStateType.Run.getValue(), "="));//likeAnywhere
        }
        return filter;
    }

    private List<FilterCondition> fetchUserNameFiler(String userName, boolean ifCheck) {

        List filter = new ArrayList();
        filter.add(new FilterCondition("userName", userName, "="));//likeAnywhere
        if (ifCheck) {
            filter.add(new FilterCondition("userState", UserStateType.Run.getValue(), "="));
        }
        return filter;
    }
    private List<FilterCondition> fetchUserRegisterFiler(String userName) {

        List filter = new ArrayList();
        filter.add(new FilterCondition("userName", userName, "="));//likeAnywhere

//        filter.add(new FilterCondition("userState", UserStateType.Failed, "!="));//UserStateType.Run.getValue()
//        filter.add(new FilterCondition("userState", new Integer[] {UserStateType.Checking.getValue(), UserStateType.Passed.getValue(), UserStateType.Run.getValue(), UserStateType.Stop.getValue()}, "in"));//UserStateType.Run.getValue()
        return filter;
    }

    private List<FilterCondition> fetchUserRegisterPhoneFiler(String userTel) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("userTel", userTel, "="));//likeAnywhere
        filter.add(new FilterCondition("userState", UserStateType.Failed, "!="));//UserStateType.Run.getValue()
//        filter.add(new FilterCondition("userState", new Integer[] {UserStateType.Checking.getValue(), UserStateType.Passed.getValue(), UserStateType.Run.getValue(), UserStateType.Stop.getValue()}, "in"));//UserStateType.Run.getValue()
        return filter;
    }
}
