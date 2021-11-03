package com.mabo.framework.source.annotation.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author mabo
 * @Description   通过bean的value来进行注入
 * 想要通过该方式注入bean，注解的属性必须有set方法并且改方法被bean注解
 * set方法必须严格按 照驼峰命名法满足好是IDE自动生成，否则有可能注入失败
 */
@Target({ElementType.FIELD})   //标识只能再方法上使用该注解
@Retention(RetentionPolicy.RUNTIME) //代码执行的时候使用
public @interface SetBean {
    public String value();
}
