package com.mabo.demo.beanDemo;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.bean.Parent;
import com.mabo.framework.source.utils.Log;

//注入bean
@Bean
//指名父类，否则父类继承的属性为空
@Parent("baseDao")
public class LoginDao extends BaseDao{
    private Log log=new Log(LoginDao.class);
    /**
     * @Author mabo
     * @Description   验证当前用户名是否存在
     */
    public String isHaveUname(String uname){
       return "LoginDao收到的内容为："+uname;
    }
}
