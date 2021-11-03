package com.mabo.framework.source.annotation.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author mabo
 * @Description   用于注入bean来使用
 * 如果不传入参数，bean.value默认为类名的首字母小写
 * 例如     类名 StudentFactory 自动生成value值为 studentFactory
 */

@Target({ElementType.TYPE})   //标识只能再方法上使用该注解
@Retention(RetentionPolicy.RUNTIME) //代码执行的时候使用
public @interface Bean {
    public String value() default "";
}
