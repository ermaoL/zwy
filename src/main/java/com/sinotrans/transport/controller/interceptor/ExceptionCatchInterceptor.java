package com.sinotrans.transport.controller.interceptor;

import com.google.gson.Gson;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.exception.RestException;
import com.sinotrans.transport.common.util.IpUtils;
import com.sinotrans.transport.dto.common.RestResponse;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by emi on 2016/5/13.
 */
public class ExceptionCatchInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(ExceptionCatchInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String agent = request.getHeader("User-Agent");
        Enumeration parameterNames = request.getParameterNames();
        StringBuilder sb = new StringBuilder();
        String params;
        while(parameterNames.hasMoreElements()) {
            params = (String)parameterNames.nextElement();
            String[] values = request.getParameterValues(params);
            sb.append("[").append(params).append(":").append(ArrayUtils.toString(values)).append("]");
        }
        System.out.println("-----------------------------------------------------------------------------------------------------");
        params = StringUtils.isNotEmpty(sb.toString())?sb.insert(0, "\n").toString():"";
//        LOG.info("[{}] =>[{}] [{}]\n[{}]{}", new Object[]{IpUtils.getIpAddress(request), request.getMethod(), request.getRequestURL(), agent, params});
        LOG.info("[" + IpUtils.getIpAddress(request) + "] =>[" + request.getMethod() + "] [" + request.getRequestURL() + "]\n[" + agent + "]{" + params + "}");
        request.setAttribute("_START_TIME", Long.valueOf(System.currentTimeMillis()));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        super.afterCompletion(request, response, handler, ex);
        System.out.println("-----------===============================================================================---------------");
        int status = response.getStatus();
        RestResponse restResponse = null;

        if (200 == status || 500 == status) {
            if (null == ex) {
                if (200 == status) {
                    return;
                }
                restResponse = new RestResponse(ErrorCode.CODE_500, "服务器内部异常，请联系管理员");
            } else if (ex instanceof RestException) {
                RestException mae2 = (RestException)ex;
                restResponse = new RestResponse(mae2.getCode(), mae2.getMessage());
            } else {
                restResponse = new RestResponse(ErrorCode.CODE_500, "服务器出问题了，请联系管理员");
            }
        } else {
            if (400 == status) {
                restResponse = new RestResponse(ErrorCode.CODE_400, "请求错误,请检查请求参数与格式");
            } else if (404 == status) {
                restResponse = new RestResponse(ErrorCode.CODE_404, "路径错误");
            } else if (405 == status) {
                restResponse = new RestResponse(ErrorCode.CODE_405, "请求方法错误");
            } else {
                restResponse = new RestResponse(Integer.parseInt("90" + response.getStatus()), "特殊异常出现");
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(new Gson().toJson(restResponse));
            LOG.debug(restResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }

        long start = Long.parseLong(request.getAttribute("_START_TIME").toString());
        LOG.info("[" + IpUtils.getIpAddress(request) + "] =>[" + request.getMethod() + "] [" + request.getRequestURL() + "] cost[" + Long.valueOf(System.currentTimeMillis() - start) + "]ms");
//        LOG.info("[{}] =>[{}] [{}] cost[{}]ms", new Object[]{IpUtils.getIpAddress(request), request.getMethod(), request.getRequestURL(), Long.valueOf(System.currentTimeMillis() - start)});
    }

}
