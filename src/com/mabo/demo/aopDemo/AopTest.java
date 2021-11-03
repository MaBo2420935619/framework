package com.mabo.demo.aopDemo;

import com.mabo.framework.source.annotation.aop.CutPoint;
import com.mabo.framework.source.annotation.bean.Bean;

//切点所在的类必须先注入，目的为了提高切面的效率
@Bean()
public class AopTest {
    private String name="mabo";
    private String pwd="123";

    public String getName() {
        return name;
    }

    public String getPwd() {

        return pwd;
    }
    //定义切点
    @CutPoint(aopId = "1")
    public void test(){
        System.out.println("这里有切点");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
