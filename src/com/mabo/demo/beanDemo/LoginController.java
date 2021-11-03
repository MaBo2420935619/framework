package com.mabo.demo.beanDemo;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.bean.SetBean;
import com.mabo.framework.source.reflect.bean.BeanFactory;

//注入该bean,不写参数，默认为类名的首字母小写
@Bean()
public class LoginController {
    public static void main(String[] args) {
        LoginController loginController = (LoginController) BeanFactory.getBean("loginController");
        loginController.login();
    }
    //给属性注入bean
    @SetBean("loginService")
    private LoginService loginService;

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

 /**
  * @Author mabo
  * @Description   测试获取到了bean
  */
    public void login(){
        loginService.login("mabo");
        System.out.println("LoginController执行结束");
    }


}
