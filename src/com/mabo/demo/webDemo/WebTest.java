package com.mabo.demo.webDemo;

import com.mabo.framework.source.reflect.bean.BeanFactory;
import com.mabo.framework.source.socket.SocketServer;

public class WebTest {
    public static void main(String[] args) {
        //浏览器启动后输入 http://localhost:8888/login?uname=mabo&upwd=123456
        //就可以看到请求成功，证明web项目建立成功
        SocketServer server= (SocketServer) BeanFactory.getBean("socketServer");
        server.startServer();
    }
}
