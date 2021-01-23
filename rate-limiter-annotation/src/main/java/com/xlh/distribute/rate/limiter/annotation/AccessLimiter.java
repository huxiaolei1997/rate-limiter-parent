package com.xlh.distribute.rate.limiter.annotation;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

/**
 * 用途描述
 *
 * @author 胡晓磊
 * @company xxx
 * @date 2021年01月23日 15:30 胡晓磊 Exp $
 */
@Service
@Slf4j
@Deprecated
public class AccessLimiter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisScript<Boolean> rateLimitLua;

    public void limitAccess(String key, Integer limit) {
            // 调用lua脚本
        boolean acquired = stringRedisTemplate.execute(rateLimitLua
                , Lists.newArrayList(key) // key
                , limit.toString()); // value列表
        if (!acquired) {
            throw new RuntimeException("Your access is blocked");
        }
    }


}
