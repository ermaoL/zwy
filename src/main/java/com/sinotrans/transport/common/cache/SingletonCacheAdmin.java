package com.sinotrans.transport.common.cache;

import com.sinotrans.transport.common.CommonConstant;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.common.exception.CommonException;
import com.sinotrans.transport.common.util.MD5Utils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by emi on 2016/5/31.
 */
public class SingletonCacheAdmin {

    private static Logger logger = Logger.getLogger(SingletonCache.class);

    private static ConcurrentHashMap<String, TokenCacheAdmin> adminMap = new ConcurrentHashMap();


    public static CheckResultAdminVo checkToken(String adminName, String token, String authType, String ipStr) {

        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(adminName) || StringUtils.isEmpty(authType)) {
            return new CheckResultAdminVo(ErrorCode.NOT_LOGIN, "管理员未登录，请先登录");
        }
        TokenCacheAdmin tokenCacheAdmin = adminMap.get(fetchKey(authType, adminName));
        if (null == tokenCacheAdmin) {
            return new CheckResultAdminVo(ErrorCode.TOKEN_ERROR_REDIRECT_LOGIN, "管理员异常，请重新登录");
        }
        if (!token.equals(tokenCacheAdmin.getToken())) {
            return new CheckResultAdminVo(ErrorCode.NAME_ERROR_REDIRECT_LOGIN, "管理员异常，请重新登录");
        }
        if (!authType.equals(tokenCacheAdmin.getCompanyType().getCode())) {
            return new CheckResultAdminVo(ErrorCode.AUTH_TYPE_NOT_EXIST, "管理员授权异常，请重新登录");
        }
        if (System.currentTimeMillis() - tokenCacheAdmin.getLastLoginTime() > CommonConstant.TIME_INTERVAL) {
            return new CheckResultAdminVo(ErrorCode.TOKEN_TIME_OUT,"管理员离开太久，请重新登录");
        }
        if (StringUtils.isEmpty(ipStr)) {
            return new CheckResultAdminVo(ErrorCode.IP_NULL, "请求相关信息获取失败，请重新登录");
        }
        if (!ipStr.equals(tokenCacheAdmin.getIpStr())) {
            return new CheckResultAdminVo(ErrorCode.IP_ERROR_REDIRECT_LOGIN, "管理员IP异常，请重新登录");
        }

        updateCount(tokenCacheAdmin);
        return new CheckResultAdminVo(tokenCacheAdmin.getAdminCode(), tokenCacheAdmin.getAdminName(), tokenCacheAdmin.getCompanyType());
    }



    public static AdminResultVo putUpdateData(String adminCode, String adminName, CompanyType companyType, String ipAddress) {
        String md5Token = "T_" + MD5Utils.string2MD5(adminName + System.currentTimeMillis());
//        if (companyType == CompanyType.AdminAll) {
//            if (StringUtils.isEmpty(adminName) || StringUtils.isEmpty(ipAddress)) {
//                throw new CommonException(ErrorCode.NULL_REGISTER_TOKEN_INFO,"令牌信息不能为空", "管理员登录时, adminName ipAddress 其中之一为空", logger);
//            }
//        } else {
//        }
        if (StringUtils.isEmpty(adminCode) || StringUtils.isEmpty(adminName) || StringUtils.isEmpty(ipAddress)) {
            throw new CommonException(ErrorCode.NULL_REGISTER_TOKEN_INFO,"令牌信息不能为空", "管理员登录时, adminCode adminName ipAddress 其中之一为空", logger);
        }

        String key = fetchKey(companyType.getCode(), adminCode.trim());
        TokenCacheAdmin tokenCache = adminMap.get(key);
        if (null != tokenCache) {
            tokenCache.updateSelf(adminCode, adminName, companyType, md5Token, ipAddress);
            adminMap.replace(key, tokenCache);
        } else {
            tokenCache = new TokenCacheAdmin(adminCode, adminName, companyType, md5Token, ipAddress);
            adminMap.put(key, tokenCache);
        }
        return new AdminResultVo(tokenCache.getToken(), tokenCache.getAdminCode(), companyType.getCode(), tokenCache.getAdminName());
    }


    private static void updateCount(TokenCacheAdmin tokenCacheAdmin) {
        tokenCacheAdmin.setCount(new AtomicInteger(tokenCacheAdmin.getCount().incrementAndGet()));
        tokenCacheAdmin.setLastLoginTime(System.currentTimeMillis());
    }

    private static String fetchKey(String authType, String adminName) {
        return (authType + "_" + adminName).trim();
    }

}
