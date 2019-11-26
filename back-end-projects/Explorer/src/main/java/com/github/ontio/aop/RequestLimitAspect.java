package com.github.ontio.aop;

import com.github.ontio.config.ParamsConfig;
import com.github.ontio.exception.ExplorerException;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/15
 */
@Component
@Aspect
@Slf4j
public class RequestLimitAspect {

    private final ParamsConfig paramsConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RequestLimitAspect(ParamsConfig paramsConfig, RedisTemplate<String, Object> redisTemplate) {
        this.paramsConfig = paramsConfig;
        this.redisTemplate = redisTemplate;
    }

    @Before("execution(public * com.github.ontio.controller.*.*(..)) && @annotation(limit)")
    public void requestLimit(JoinPoint joinpoint, RequestLimit limit) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ip = Helper.getHttpReqRealIp(request);
        String url = request.getRequestURL().toString();
        String queryStr = request.getQueryString();
        if(Helper.isEmptyOrNull(queryStr)){
            queryStr = "";
        }
        String key = "req_limit_".concat(url).concat(queryStr).concat("_").concat(ip);

        //加1后看看值
        long count = redisTemplate.opsForValue().increment(key, 1);
        //刚创建
        if (count == 1) {
            //设置1分钟过期
            redisTemplate.expire(key, paramsConfig.REQLIMIT_EXPIRE_MILLISECOND, TimeUnit.MILLISECONDS);
        }
        if (count > limit.count()) {
            log.warn("用户IP[" + ip + "]访问地址[" + url + "?" + queryStr + "]超过了限定的次数[" + limit.count() + "]");
            throw new ExplorerException(ErrorInfo.REQ_TIME_EXCEED.code(), ErrorInfo.REQ_TIME_EXCEED.desc(), false);
        }
    }

}
