package com.sinotrans.transport.common.cache;

import com.sinotrans.transport.common.CommonConstant;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.exception.CommonException;
import com.sinotrans.transport.common.util.MD5Utils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by emi on 2016/5/10.
 */
public class SingletonCache {

    private static Logger logger = Logger.getLogger(SingletonCache.class);
    private static ConcurrentHashMap<String, TokenCache> map = new ConcurrentHashMap();

    public static ConcurrentHashMap getMap() {
        return map;
    }

//    public static TokenCache putData(TokenCache tokenCache) {
//
//        map.putIfAbsent(tokenCache.getUserName(),tokenCache);
//        return map.get(tokenCache.getUserName());
//    }

    public static CheckResultVo checkToken(String userName, String token, String ipStr) {

        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userName)) {
            return new CheckResultVo(ErrorCode.NOT_LOGIN, "用户未登录，请先登录");
        }
        TokenCache tokenCache = map.get(userName);
        if (null == tokenCache) {
            return new CheckResultVo(ErrorCode.NAME_ERROR_REDIRECT_LOGIN, "用户异常，请重新登录");
        }
        if (!token.equals(tokenCache.getToken())) {
            return new CheckResultVo(ErrorCode.TOKEN_ERROR_REDIRECT_LOGIN, "用户异常，请重新登录");
        }
        if (System.currentTimeMillis() - tokenCache.getLastLoginTime() > CommonConstant.TIME_INTERVAL) {
            return new CheckResultVo(ErrorCode.TOKEN_TIME_OUT,"用户离开太久，请重新登录");
        }
        if (StringUtils.isEmpty(ipStr)) {
            return new CheckResultVo(ErrorCode.IP_NULL, "请求相关信息获取失败，请重新登录");
        }
        if (!ipStr.equals(tokenCache.getIpStr())) {
            return new CheckResultVo(ErrorCode.IP_ERROR_REDIRECT_LOGIN, "用户IP异常，请重新登录");
        }

        updateCount(tokenCache);
        return new CheckResultVo(tokenCache.getUserId());
    }

    public static String putUpdateData(long userId, String userName, String ipAddress) {

        String md5Token = "T_" + MD5Utils.string2MD5(userName + System.currentTimeMillis());
        if (0 == userId || StringUtils.isEmpty(userName) || StringUtils.isEmpty(ipAddress)) {
            throw new CommonException(ErrorCode.NULL_REGISTER_TOKEN_INFO,"令牌信息不能为空", "登录或注册时, userId userName ipAddress 其中之一为空", logger);
        }
        TokenCache tokenCache = map.get(userName);
        if (null != tokenCache) {
            tokenCache.updateSelf(userId, userName, md5Token, ipAddress);
            map.replace(userName, tokenCache);
        } else {
            tokenCache = new TokenCache(userId, userName, md5Token, ipAddress);
            map.put(userName, tokenCache);
        }
        return tokenCache.getToken();
    }

    private static void updateCount(TokenCache tokenCache) {
        tokenCache.setCount(new AtomicInteger(tokenCache.getCount().incrementAndGet()));
        tokenCache.setLastLoginTime(System.currentTimeMillis());
    }

}
