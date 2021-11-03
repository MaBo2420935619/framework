package com.mabo.framework.source.reflect.quartz;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.quartz.Quartz;
import com.mabo.framework.source.initInterface.InitInterface;
import com.mabo.framework.source.reflect.bean.BeanFactory;
import com.mabo.framework.source.utils.Log;
import com.mabo.framework.source.utils.StringUtil;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuartzReflect {
    private static Log log=new Log(QuartzReflect.class);

    /**
     * @Author mabo
     * @Description   用于反射定时器
     */
    public static void reflect(){
        List<Method> methods = InitInterface.annotationOnMethod(Quartz.class);
        for (Method method : methods) {
            method.setAccessible(true);//设置方法为可执行的
            if (method.isAnnotationPresent(Quartz.class)) {
                Class declaringClass = method.getDeclaringClass();
                String simpleName =null;
                Bean bean = (Bean)declaringClass.getAnnotation(Bean.class);
                if(bean==null||bean.value().equals("")){
                    simpleName = StringUtil.toLowerCaseFirstOne(declaringClass.getSimpleName());
                }else{
                    simpleName=bean.value();
                }
                Object o = BeanFactory.getBean(simpleName);
                //获取注解的接口
                Quartz mt = method.getAnnotation(Quartz.class);
                //获取注解的参数
                Object finalO = o;
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            method.invoke(finalO);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error(method.getName()+"()方法执行失败");
                        }
                    }
                };
                ScheduledExecutorService service = Executors
                        .newSingleThreadScheduledExecutor();
                // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
                service.scheduleAtFixedRate(runnable, mt.waitSecond(), mt.time(), TimeUnit.SECONDS);

            }
        }
        log.info("多线程定时器初始化成功");
    }
}
