package com.xlh.distribute.rate.limiter.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * 用途描述
 *
 * @author 胡晓磊
 * @company xxx
 * @date 2021年01月23日 15:38 胡晓磊 Exp $
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }


    @Bean
    public DefaultRedisScript loadRedisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setLocation(new ClassPathResource("ratelimiter.lua"));
        redisScript.setResultType(java.lang.Boolean.class);
        return redisScript;
    }
}
