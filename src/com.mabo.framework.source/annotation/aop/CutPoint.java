package com.mabo.framework.source.annotation.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author mabo
 * @Description   用来标注切点,和aop标注的类的一一对应
 * @CutPoint.aopId和@Aop.aopId一一对应
 */
@Target({ElementType.METHOD})   //标识只能再方法上使用该注解
@Retention(RetentionPolicy.RUNTIME) //代码执行的时候使用
public @interface CutPoint {
    public String aopId();
}
