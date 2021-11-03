package com.mabo.framework.source.start;
import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.bean.SetBean;
import com.mabo.framework.source.socket.SocketServer;

/**
 * @Author mabo
 * @Description   javaWeb项目可从此处开始
 */
@Bean
public class Framework {
    @SetBean("socketServer")
    private static SocketServer socketServer;

    public SocketServer getSocketServer() {
        return socketServer;
    }

    public void setSocketServer(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    public static void startFramework(Class clazz){
        socketServer.startServer();
    }
}
