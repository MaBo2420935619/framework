package com.mabo.demo.beanDemo;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.bean.SetBean;


@Bean("loginService")
public class LoginService {
    @SetBean("loginDao")
    private LoginDao loginDao;
    public void setLoginDao(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    /**
     * @Author mabo
     * @Description   判断是否登录
     */
    public  void login(String uname){
        String haveUname = loginDao.isHaveUname(uname);

        System.out.println(haveUname);
        Student student = loginDao.getStudent();
        System.out.println("获取到了继承的basedao的属性学生,");
        student.say();

    }



}
