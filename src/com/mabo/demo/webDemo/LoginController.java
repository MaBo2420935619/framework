package com.mabo.demo.webDemo;

import com.alibaba.fastjson.JSONObject;
import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.controller.RequestMapping;
import com.mabo.framework.source.utils.MapUtil;
import java.util.Map;

/**
 * @Author mabo
 * @Description   在这里写请求方法
 */
//使用 loginController2 使防止重复注入，项目会报错
@Bean("loginController2")
public class LoginController {

    /**
     * @Author mabo
     * @Description   登录操作
     */
    @RequestMapping("/login")
    public Object login(Map map){
        System.out.println("接受到的请求内容为"+map.toString());
        String uname = MapUtil.getMapString(map,"uname");
        String upwd = MapUtil.getMapString(map,"upwd");
        JSONObject json=new JSONObject();
        json.put("login","我收到了你的请求");
        json.put("uname",uname);
        json.put("upwd",upwd);
        //此处测试aop的动态代理结束
        return  JSONObject.toJSON(json);
    }


}
