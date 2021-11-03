package com.mabo.framework.source.reflect.aop;

import com.mabo.framework.source.annotation.aop.Aop;
import com.mabo.framework.source.enums.AopType;
import com.mabo.framework.source.initInterface.InitInterface;
import com.mabo.framework.source.reflect.bean.BeanFactory;
import com.mabo.framework.source.utils.Log;
import com.mabo.framework.source.utils.StringUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author mabo
 * @Description   实现jdk动态代理    暂时还未实现，不能使用
 *  代理对象必须实现了接口，否则无法正常代理
 *  暂时还没有使用
 */

public class CreateJdkBeanProxy implements InvocationHandler {
    private static Log log=new Log(CreateJdkBeanProxy.class);
    // 目标类，也就是被代理对象
    private  Object target;
    private String aopId;

    public void setAopId(String aopId) {
        this.aopId = aopId;
    }

    public void setTarget(Object target)
    {
        this.target = target;
    }

    public Object aop(Object proxy, Method method, Object[] args){
        List<Method> list = InitInterface.annotationOnMethod(Aop.class);
        Method aopMethod=null;
        int count=0;
        Aop annotation =null;
        for (Method me: list) {
            if (me.isAnnotationPresent(Aop.class)) {
                //获取注解的接口
                annotation=me.getAnnotation(Aop.class);
                if (annotation.aopid().equals(aopId)){
                    count++;
                    aopMethod=me;
                }
            }
        }
        if (count==1){
            // 这里可以做增强
            AopType aopType = annotation.aopType();
            aopMethod.setAccessible(true);//设置方法为可执行的
            Class declaringClass = aopMethod.getDeclaringClass();
            String simpleName = StringUtil.toLowerCaseFirstOne(declaringClass.getSimpleName());
            Object o = BeanFactory.getBean(simpleName);
            Object result=null;
            return result;
        }
        else if (count<1){
            log.error("找不到aop,id:"+aopId);
            return null;
        }
        else if (count>1){
            log.error("重复的aop,id"+aopId);
            return null;
        }
        else{
            log.error("系统未知错误");
            return null;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        //此处进行增强
        Object aop = aop(proxy, method, args);
        return aop;
    }

}
