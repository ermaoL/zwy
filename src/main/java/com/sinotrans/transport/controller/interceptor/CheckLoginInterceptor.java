package com.sinotrans.transport.controller.interceptor;

import com.google.gson.Gson;
import com.sinotrans.transport.common.cache.CheckResultVo;
import com.sinotrans.transport.common.cache.SingletonCache;
import com.sinotrans.transport.common.util.IpUtils;
import com.sinotrans.transport.dto.common.RestResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by emi on 2016/5/10.
 */
public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userName = request.getHeader("userName");
        String token = request.getHeader("token");
        String ipStr = IpUtils.getIpAddress(request);

        CheckResultVo tokenCheck = SingletonCache.checkToken(userName, token, ipStr);
        if (tokenCheck.isSuccess()) {
            request.setAttribute("userId", tokenCheck.getUserId());
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
