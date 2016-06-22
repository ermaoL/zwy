package com.sinotrans.transport.controller;

import com.sinotrans.transport.common.exception.RestException;
import com.sinotrans.transport.common.util.IpUtils;
import com.sinotrans.transport.controller.handler.ExceptionController;
import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by emi on 2016/5/10.
 */
@Controller
@RequestMapping(value = "/api/user")
public class UserController extends ExceptionController {

    @Autowired
    private UserService userService;

    //访问次数  token创建时间   最后修改时间  userId
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public UserRegisterResponse register(@Valid UserRegisterRequest registerRequest, HttpServletRequest request) {
        return userService.register(registerRequest, IpUtils.getIpAddress(request));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public UserLoginResponse login(@Valid UserLoginRequest loginRequest, HttpServletRequest request) {
        return userService.login(loginRequest.getUserName(), loginRequest.getUserPass(), IpUtils.getIpAddress(request));
    }

    @RequestMapping(value = "/pass/changes", method = RequestMethod.POST)
    @ResponseBody
    public UserAlterPassResponse alterPass(@Valid UserAlterPassRequest alterPassRequest, HttpServletRequest request) {
        long userId = (Long)request.getAttribute("userId");
        return userService.alterPass(alterPassRequest, userId);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public UserLoginResponse test(HttpServletRequest request) {
        throw new RestException(22,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

    }

    @RequestMapping(value = "/bind/{userId:\\d+}/{companyTYpe:\\d}", method = RequestMethod.POST)
    @ResponseBody
    public boolean bind(@PathVariable("userId") Long userId, @PathVariable("companyTYpe") int companyType) {
        return userService.bind(userId, companyType);
    }

}
