package com.sinotrans.transport.service;

import com.sinotrans.transport.dto.*;

/**
 * Created by emi on 2016/5/10.
 */
public interface UserService {
    UserRegisterResponse register(UserRegisterRequest request, String ipAddress);

    UserLoginResponse login(String userName, String password, String ipAddress);

    UserAlterPassResponse alterPass(UserAlterPassRequest alterPassRequest, long userId);

    boolean bind(Long userId, int companyType);
}
