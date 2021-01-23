package com.xlh.distribute.rate.limiter.annotation;

import java.lang.annotation.*;

/**
 * 用途描述
 *
 * @author 胡晓磊
 * @company xxx
 * @date 2021年01月23日 16:29 胡晓磊 Exp $
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiterAnnotation {
    int limit();

    String methodKey() default "";
}
