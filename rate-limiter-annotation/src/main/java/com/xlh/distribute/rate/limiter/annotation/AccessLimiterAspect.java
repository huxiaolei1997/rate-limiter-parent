package com.xlh.distribute.rate.limiter.annotation;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 用途描述
 *
 * @author 胡晓磊
 * @company xxx
 * @date 2021年01月23日 16:31 胡晓磊 Exp $
 */
@Slf4j
@Aspect
@Component
public class AccessLimiterAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisScript<Boolean> rateLimitLua;


    @Pointcut("@annotation(com.xlh.distribute.rate.limiter.annotation.AccessLimiterAnnotation)")
    public void cut() {
        log.info("cut");
    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        // 1.获取方法签名，作为method key
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        AccessLimiterAnnotation accessLimiterAnnotation = method.getAnnotation(AccessLimiterAnnotation.class);

        if (null == accessLimiterAnnotation) {
            return;
        }

        String key = accessLimiterAnnotation.methodKey();
        Integer limit = accessLimiterAnnotation.limit();

        if (StringUtils.isEmpty(key)) {
            Class[] type = method.getParameterTypes();
            key = method.getName();
            if (type != null) {
                String paramTypes = Arrays.stream(type).map(Class::getName).collect(Collectors.joining(","));
                log.info("param types: {}", paramTypes);
                key += "#" + paramTypes;
            }
        }
        boolean acquired = stringRedisTemplate.execute(rateLimitLua
                , Lists.newArrayList(key) // key
                , limit.toString()); // value列表
        if (!acquired) {
            throw new RuntimeException("Your access is blocked");
        }
        // 2.调用
    }
}
