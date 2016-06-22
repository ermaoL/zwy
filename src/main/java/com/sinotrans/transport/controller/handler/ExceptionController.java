package com.sinotrans.transport.controller.handler;

import com.sinotrans.transport.common.exception.RestException;
import com.sinotrans.transport.common.util.IpUtils;
import com.sinotrans.transport.dto.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by emi on 2016/5/16.
 */
@Controller
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);
    private static int SERVER_ERROR_CODE = 100001;
    private static String SERVER_ERROR_MSG = "服务器内部异常";
    private static int VALID_PARAM_ERROR_CODE = 100002;
    private static int REQUEST_URL_ERROR_CODE = 100003;
    private static String REQUEST_URL_ERROR_MSG = "请求路径不合法";


//    @org.springframework.web.bind.annotation.ExceptionHandler
    protected String exp(HttpServletRequest request, Exception e) {
        return "/error";
    }

    @RequestMapping({"/error"})
    @ResponseBody
    public RestResponse exception(HttpServletRequest request, HttpServletResponse response) {
        int code = 0;
        String message = "";

        try {
            response.setStatus(200);
            Map e = this.getErrorAttributes(request);
            if(e != null) {
                if(e.get("code") != null) {
                    code = Integer.parseInt(e.get("code").toString());
                }

                if(e.get("message") != null) {
                    message = e.get("message").toString();
                }

                if(e.get("debugMessage") != null) {
                    String path = (String)request.getAttribute("javax.servlet.error.request_uri");
                    LOGGER.error(String.format("[IP:%s][path:%s][exception:%s]%s", new Object[]{IpUtils.getIpAddress(request), path, e.get("exception"), e.get("debugMessage").toString()}));
                }
            }

            return new RestResponse(code, message);
        } catch (Exception var7) {
            LOGGER.error("SystemErrorController-Exception:" + var7.getMessage());
            return new RestResponse(SERVER_ERROR_CODE, SERVER_ERROR_MSG);
        }
    }


    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        LinkedHashMap errorAttributes = new LinkedHashMap();
        this.addErrorDetails(errorAttributes, requestAttributes);
        return errorAttributes;
    }

    private void addErrorDetails(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        Throwable error = this.getError(requestAttributes);
        if(error != null) {
            if(error instanceof RestException) {
                RestException mae2 = (RestException)error;
                errorAttributes.put("code", Integer.valueOf(mae2.getCode()));
                errorAttributes.put("message", mae2.getMessage());
//                errorAttributes.put("debugMessage", mae2.getDebugMessage());
            } else {
                String message;
                if(error instanceof BindException) {
                    BindException mae21 = (BindException)error;
                    message = this.getBindMessage(mae21.getMessage());
                    errorAttributes.put("code", Integer.valueOf(VALID_PARAM_ERROR_CODE));
                    errorAttributes.put("message", message);
//                    errorAttributes.put("debugMessage", message);
                } else if(error instanceof MethodArgumentNotValidException) {
                    MethodArgumentNotValidException mae22 = (MethodArgumentNotValidException)error;
                    message = this.getBindMessage(mae22.getMessage());
                    errorAttributes.put("code", Integer.valueOf(VALID_PARAM_ERROR_CODE));
                    errorAttributes.put("message", message);
//                    errorAttributes.put("debugMessage", message);
                } else {
                    errorAttributes.put("code", Integer.valueOf(SERVER_ERROR_CODE));
                    errorAttributes.put("message", SERVER_ERROR_MSG);
//                    errorAttributes.put("debugMessage", error.getMessage());
                }
            }

            errorAttributes.put("exception", error.getClass().getName());
        } else {
            errorAttributes.put("code", Integer.valueOf(REQUEST_URL_ERROR_CODE));
            errorAttributes.put("message", REQUEST_URL_ERROR_MSG);
            errorAttributes.put("debugMessage", REQUEST_URL_ERROR_MSG);
        }

    }

    private Throwable getError(RequestAttributes requestAttributes) {
        Throwable exception = (Throwable)requestAttributes.getAttribute(null + ".ERROR", 0);
        if(exception == null) {
            exception = (Throwable)requestAttributes.getAttribute("javax.servlet.error.exception", 0);
        }

        return exception;
    }

    private String getBindMessage(String str) {
        if(StringUtils.hasText(str)) {
            String[] sa = str.split("message");
            if(sa != null && sa.length > 0) {
                for(int i = sa.length - 1; i >= 0; --i) {
                    if(sa[i].getBytes().length != sa[i].length()) {
                        str = sa[i].trim().replace("[", "");
                        String[] st = str.split("]");
                        if(st != null && st.length > 0) {
                            str = st[0];
                        }
                        break;
                    }
                }
            }
        }

        return str;
    }
}
