package com.mabo.demo.beanDemo;

import com.mabo.framework.source.annotation.bean.Bean;
/**
 * @Author mabo
 * @Description   学生类
 */

@Bean
public class Student {
    private String name;
    private String pwd;

    public Student() {
        this.name = "mabo";
        this.pwd="123";
    }
    public Student(String name, String pwd) {
        this.name = name;
        this.pwd=pwd;
    }
    public void say(){
        System.out.println("这是学生类");
    }

}
