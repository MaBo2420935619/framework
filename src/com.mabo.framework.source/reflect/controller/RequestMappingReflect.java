package com.mabo.framework.source.reflect.controller;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.controller.RequestMapping;
import com.mabo.framework.source.enums.RequestType;
import com.mabo.framework.source.initInterface.InitInterface;
import com.mabo.framework.source.reflect.bean.BeanFactory;
import com.mabo.framework.source.utils.Log;
import com.mabo.framework.source.utils.StringUtil;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author mabo
 * @Description   RequestMapping接口的反射类
 *
 */
public class RequestMappingReflect {
    private static Log log=new Log(RequestMappingReflect.class);

/**
 * @Author mabo
 * @Description
 * String requestMapping: RequestMapping接口的反射类的请求类型
 * RequestType requestType: 发送的是什么类型的请求POST/GET/PUT
 */
    public static Object getReflect(String requestMapping,RequestType requestType, Map map) {
        List<Method> methods = InitInterface.annotationOnMethod(RequestMapping.class);
        for (Method method : methods) {
            method.setAccessible(true);//设置方法为可执行的
            if (method.isAnnotationPresent(RequestMapping.class)) {
                Class declaringClass = method.getDeclaringClass();
                Object o = null;
                //获取注解的接口
                RequestMapping mt = method.getAnnotation(RequestMapping.class);
                //获取注解的参数
                String value = mt.value();
                RequestType rt = mt.requestType();
                if (requestMapping.equals(value)&&requestType.equals(rt))
                {
                    //实例化类
                    try {
                        o = declaringClass.newInstance();
                    } catch (Exception e) {
                        log.info(declaringClass.getName()+"()类实例化失败");
                    }
                    //反射执行方法
                    try {
                        //重要
                        //获取bean工厂的bean
                        Bean annotation = o.getClass().getAnnotation(Bean.class);
                        String value1 = annotation.value();
                        String name =null;
                        if (!value1.equals(""))
                            name=value1;
                        else{
                            name = o.getClass().getSimpleName();

                            name = StringUtil.toLowerCaseFirstOne(name);
                        }
                        Object bean = BeanFactory.getBean(name);
                        Object invoke = method.invoke(bean, map);
                        log.info(method.getName()+"()方法执行成功");
                        return invoke;
                    } catch (Exception e) {
                        log.info(method.getName()+"()方法执行失败");
                    }
                }
            }
        }
        return null;
    }
}
