package com.mabo.framework.source.socket;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.utils.Log;
import com.mabo.framework.source.utils.PropertyUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Bean
public class SocketServer {
    private static Log log=new Log(SocketServer.class);
    private static ServerSocket serverSocket;
    private static int port= Integer.parseInt(PropertyUtil.get("config/config.properties", "serverPort"));
    private static ScheduledExecutorService scheduledThreadPool= Executors.newScheduledThreadPool(Integer.parseInt(PropertyUtil.get("config/config.properties", "maxTotal")));
    static {
        try {
            serverSocket = new ServerSocket(port);
//            log.info("服务器在"+port+"端口启动成功");
        } catch (IOException e) {
            e.printStackTrace();
            log.error(port+"以及被占用，无法启动服务器");
        }
    }
    /**
     * @Author mabo
     * @Description   启动端口监听
     */
    public void startServer() {
        while (true) {//一直监听，直到受到停止的命令
            Socket socket = null;
            try {
                socket = serverSocket.accept();//如果没有请求，会一直hold在这里等待，有客户端请求的时候才会继续往下执行
                Socket finalSocket = socket;
                scheduledThreadPool.schedule(new Runnable(){
                    @Override
                    public void run() {
                        ReceiveMsg controller=new ReceiveMsg();
                        controller.receiveHttpRequest(finalSocket);
                    }
                }, 0, TimeUnit.SECONDS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
