package com.mabo.demo.aopDemo;

import com.mabo.framework.source.annotation.aop.Aop;
import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.enums.AopType;
import com.mabo.framework.source.reflect.bean.BeanFactory;

@Bean()
//为这个类的所有方法添加切点,AopType.Around标识循环切入
//可以达到不侵入式编程
//要注意切面和切点不能相互依赖
@Aop(aopid = "1",aopType = AopType.Around)
public class AopTest2 {

    public void test(){
        System.out.println("我是切面");
    }

    public static void main(String[] args) {
        AopTest2 aopTest2 = (AopTest2) BeanFactory.getBean("aopTest2");
        aopTest2.test();
    }
}
