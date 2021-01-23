package com.xlh.distribute.rate.limiter.guava.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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

    RateLimiter limiter = RateLimiter.create(2.0);


    // 非阻塞限流
    @GetMapping("/tryAcquire")
    public String tryAcquire(Integer count) {
        if (limiter.tryAcquire(count)) {
            log.info("success, rate is {}", limiter.getRate());
            return "success";
        } else {
            log.info("fail, rate is {}", limiter.getRate());
            return "fail";
        }
    }

    // 限定时间的非阻塞限流
    @GetMapping("/tryAcquireWithTimeout")
    public String tryAcquireWithTimeout(Integer count, Integer timeout) {
        if (limiter.tryAcquire(count, timeout, TimeUnit.SECONDS)) {
            log.info("success, rate is {}", limiter.getRate());
            return "success";
        } else {
            log.info("fail, rate is {}", limiter.getRate());
            return "fail";
        }
    }

    /**
     * 同步阻塞限流
     */
    @GetMapping("/acquire")
    public String acquire(Integer count) {
        limiter.acquire(count);
        log.info("success, rate is {}", limiter.getRate());
        return "success";
    }

    /**
     * Nginx专用
     */
    @GetMapping("/nginx")
    public String nginx() {
        log.info("Nginx success");
        return "success";
    }

    @GetMapping("/nginx-conn")
    public String nginxConn(@RequestParam(defaultValue = "0") int secs) {
        try {
            Thread.sleep(1000 * secs);
        } catch (Exception e) {

        }

        return "success";
    }
}
