package com.mabo.demo.beanDemo;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.bean.SetBean;

/**
 * @Author mabo
 * @Description   基本类，目的是验证被继承
 */
@Bean
public class BaseDao {
    //注入student
    @SetBean("student")
    public Student student;

    public String s="mabo";

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

}
