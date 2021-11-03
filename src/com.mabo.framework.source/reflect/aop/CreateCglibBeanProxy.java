package com.mabo.framework.source.reflect.aop;

import com.mabo.framework.source.annotation.aop.Aop;
import com.mabo.framework.source.annotation.aop.CutPoint;
import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.enums.AopType;
import com.mabo.framework.source.initInterface.InitInterface;
import com.mabo.framework.source.reflect.bean.BeanFactory;
import com.mabo.framework.source.utils.Log;
import com.mabo.framework.source.utils.StringUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author mabo
 * @Description   创建bean后，对bean进行cglib增强
 */
public class CreateCglibBeanProxy implements MethodInterceptor {
    private static Log log=new Log(CreateCglibBeanProxy.class);
    private Object target;
    //切点
    private Aop  aop;

    public Object getInstance(Object target, Aop aop) {
        this.target = target;
        this.aop=aop;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        // 设置回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    /**
     * 实现MethodInterceptor接口中重写的方法
     *
     * 回调方法
     */
    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        AopType aopType = aop.aopType();
        String aopId=aop.aopid();
        List<Method> list = InitInterface.annotationOnMethodFromBean(CutPoint.class);
        Method aopMethod=null;
        int count=0;
        CutPoint annotation =null;
        for (Method me: list) {
            if (me.isAnnotationPresent(CutPoint.class)) {
                //获取注解的接口
                annotation=me.getAnnotation(CutPoint.class);
                if (annotation.aopId().equals(aopId)){
                    count++;
                    aopMethod=me;
                }
            }
        }
        if (count==1){
            aopMethod.setAccessible(true);//设置方法为可执行的
            Class declaringClass = aopMethod.getDeclaringClass();
            String simpleName =null;
            Bean bean = (Bean)declaringClass.getAnnotation(Bean.class);
            if(bean==null||bean.value().equals("")){
                simpleName = StringUtil.toLowerCaseFirstOne(declaringClass.getSimpleName());
            }else{
                simpleName=bean.value();
            }
            Object o = BeanFactory.getBean(simpleName);
            if (aopType==AopType.Before){
                try {
                    aopMethod.invoke(o);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(aopMethod.getName()+"()方法执行失败");
                }
                //被增强之前正常执行
                Object result = proxy.invokeSuper(object, args);
                return result;
            }
            else if (aopType==AopType.After){
                //被增强之前正常执行
                Object result = proxy.invokeSuper(object, args);
                try {
                    aopMethod.invoke(o);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(aopMethod.getName()+"()方法执行失败");
                }
                return result;
            }
            else if (aopType==AopType.Around){
                try {
                    aopMethod.invoke(o);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(aopMethod.getName()+"()方法执行失败");
                }
                //被增强之前正常执行
                Object result = proxy.invokeSuper(object, args);
                try {
                    aopMethod.invoke(o);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(aopMethod.getName()+"()方法执行失败");
                }
                return result;
            }
        }
//        System.out.println("事务开始。。。");
        Object result = proxy.invokeSuper(object, args);
//        System.out.println("事务结束。。。");
        return result;
    }
}
