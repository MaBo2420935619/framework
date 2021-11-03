package com.mabo.framework.source.annotation.quartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author mabo
 * @Description   定时任务,默认使用多线程的异步方式执行
 *                  间隔时间（单位秒）
 */

@Target({ElementType.METHOD})   //标识只能再方法上使用该注解
@Retention(RetentionPolicy.RUNTIME) //代码执行的时候使用
public @interface Quartz {
    public int waitSecond() default 0;
    public int time() ;
}
