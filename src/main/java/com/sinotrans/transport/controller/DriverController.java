package com.sinotrans.transport.controller;

/**
 * Created by emi on 2016/5/30.
 */

import com.sinotrans.transport.common.exception.RestException;
import com.sinotrans.transport.dto.common.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/app/driver")
public class DriverController {


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse test(HttpServletRequest request) {
        throw new RestException(22,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

    }
}
