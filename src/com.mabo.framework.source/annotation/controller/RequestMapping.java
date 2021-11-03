package com.mabo.framework.source.annotation.controller;

import com.mabo.framework.source.enums.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author mabo
 * @Description   用于控制请求的注解
 *                只能使用用在方法上面,其他位置不生效
 *                用于控制web的请求类型,默认post
 */
@Target({ElementType.METHOD})   //标识只能再方法上使用该注解
@Retention(RetentionPolicy.RUNTIME) //代码执行的时候使用
public @interface RequestMapping {
    String value();
    RequestType requestType() default RequestType.POST;
}
