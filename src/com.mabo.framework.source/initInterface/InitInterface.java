package com.mabo.framework.source.initInterface;

import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.initClass.InitClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author mabo
 * @Descriptions 接口的初始化
 */
public class InitInterface {
    /**
     * @Author mabo
     * @Description   获取指定注解修饰的属性
     */
    public static List<Field> annotationOnField(Class clazz){
        List<Field> list=new ArrayList<>();
        List<InitClass> init = InitClass.getInitClass();
        for (InitClass initClass: init) {
            Field[] fields = initClass.getDeclaredFields();
            for (Field field: fields) {
                boolean annotationPresent = field.isAnnotationPresent(clazz);
                if(field.isAnnotationPresent(clazz)){
                    list.add(field);
                }
            }
        }
        return list;
    }


    /**
     * @Author mabo
     * @Description   获取指定注解修饰的类
     */
    public static List<Class> annotationOnClass(Class clazz){
        List<Class> list=new ArrayList<>();
        List<InitClass> init = InitClass.getInitClass();
        for (InitClass initClass: init) {
            Class aClass = initClass.getClazz();
            if (aClass.isAnnotationPresent(clazz))
                list.add(aClass);
        }
        return list;
    }

    /**
     * @Author mabo
     * @Description   获取指定注解修饰的方法
     */
    public static List<Method> annotationOnMethod(Class clazz){
        List<Method> list=new ArrayList<>();
        List<InitClass> init = InitClass.getInitClass();
        for (InitClass initClass: init) {
            Class aClass = initClass.getClazz();
            Method[] methods = aClass.getMethods();
            for (Method method: methods) {
                if(method.isAnnotationPresent(clazz)){
                    list.add(method);
                }
            }
        }
        return list;
    }
    /**
     * @Author mabo
     * @Description   获取bean工厂指定注解修饰的方法
     */
    public static List<Method> annotationOnMethodFromBean(Class clazz){
        List<Method> list=new ArrayList<>();
        List<InitClass> init = InitClass.getInitClass();
        for (InitClass initClass: init) {
            Class aClass = initClass.getClazz();
            if (aClass.isAnnotationPresent(Bean.class)){
                Method[] methods = aClass.getMethods();
                for (Method method: methods) {
                    if(method.isAnnotationPresent(clazz)){
                        list.add(method);
                    }
                }
            }
        }
        return list;
    }
}
