package com.mabo.framework.source.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutor{
    /**
     * @Author mabo
     * @Description 创建线程
     */
    public void createThread(){
        ScheduledExecutorService scheduledThreadPool= Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable(){
            @Override
            public void run() {

            }
        }, 0, TimeUnit.SECONDS);
    }

}
