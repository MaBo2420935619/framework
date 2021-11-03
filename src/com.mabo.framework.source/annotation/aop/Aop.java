package com.mabo.framework.source.annotation.aop;

import com.mabo.framework.source.enums.AopType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author mabo
 * @Description 方法在切面选择执行
 * 用于定义切点
 *
 */

@Target({ElementType.TYPE})   //标识只能再方法上使用该注解
@Retention(RetentionPolicy.RUNTIME) //代码执行的时候使用
public @interface Aop {
    //唯一的切面id
    public String aopid();
    //后置还是前置执行
    public AopType aopType();
}
