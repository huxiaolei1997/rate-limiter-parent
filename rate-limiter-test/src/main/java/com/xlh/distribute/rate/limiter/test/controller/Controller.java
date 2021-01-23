package com.xlh.distribute.rate.limiter.test.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.xlh.distribute.rate.limiter.annotation.AccessLimiter;
import com.xlh.distribute.rate.limiter.annotation.AccessLimiterAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途描述
 *
 * @author 胡晓磊
 * @company xxx
 * @date 2021年01月20日 20:53 胡晓磊 Exp $
 */
@RestController
@Slf4j
public class Controller {

    @Autowired
    private AccessLimiter accessLimiter;


    // 非阻塞限流
    @GetMapping("/test")
    public String test() {
        accessLimiter.limitAccess("ratelimiter-test", 1);
        return "success";
    }

    // 注意配置扫包路径
    @GetMapping("/test-annotation")
    @AccessLimiterAnnotation(limit = 1)
    public String testAnnotation() {
        return "success";
    }
}
