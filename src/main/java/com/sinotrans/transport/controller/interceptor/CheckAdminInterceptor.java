package com.sinotrans.transport.controller.interceptor;

import com.google.gson.Gson;
import com.sinotrans.transport.common.cache.CheckResultAdminVo;
import com.sinotrans.transport.common.cache.SingletonCacheAdmin;
import com.sinotrans.transport.common.util.IpUtils;
import com.sinotrans.transport.dto.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by emi on 2016/5/30.
 */
public class CheckAdminInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String adminCode = request.getHeader("adminCode");
        String token = request.getHeader("token");
        String authType = request.getHeader("authType");
        String ipStr = IpUtils.getIpAddress(request);

        CheckResultAdminVo tokenCheck = SingletonCacheAdmin.checkToken(adminCode, token, authType, ipStr);
        if (tokenCheck.isSuccess()) {
            request.setAttribute("adminCode", tokenCheck.getAdminCode());
            request.setAttribute("adminName", tokenCheck.getAdminName());
            request.setAttribute("companyType", tokenCheck.getCompanyType());
            return super.preHandle(request, response, handler);
        } else {
            RestResponse restResponse = new RestResponse(tokenCheck.getErrorCode(), tokenCheck.getErrorMsg());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(new Gson().toJson(restResponse));
                LOGGER.debug(restResponse.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return false;
        }
    }
}
