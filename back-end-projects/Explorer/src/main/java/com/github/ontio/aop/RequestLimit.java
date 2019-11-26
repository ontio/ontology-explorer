package com.github.ontio.aop;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
//最高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {

    /**
     * 允许访问的次数
     */
    int count() default 10;

    /**
     * 时间段，多少时间段内运行访问count次
     */
    long time() default 60000;

}
