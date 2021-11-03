package com.mabo.framework.source.initClass;

import com.mabo.framework.source.utils.PackageUtil;
import com.mabo.framework.source.utils.PropertyUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author mabo
 * @Description   用于实现基本注解的底层源码
 */
public class InitClass{
    private static String rootPath= PropertyUtil.get("config/config.properties", "rootPath");
    //类
    private Class clazz;

    //类名及其包名
    private String name;

    //类名
    private  String simpleName;

    //实体的所有方法
    private Method[] methods;

    //实体的所有属性
    private Field[] declaredFields;

    //是否为接口
    private boolean anInterface;

    //是否为注解
    private boolean anAnnotation;

    //是否为枚举
    private boolean anEnum;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public Field[] getDeclaredFields() {
        return declaredFields;
    }

    public void setDeclaredFields(Field[] declaredFields) {
        this.declaredFields = declaredFields;
    }

    public boolean isAnInterface() {
        return anInterface;
    }

    public void setAnInterface(boolean anInterface) {
        this.anInterface = anInterface;
    }

    public boolean isAnAnnotation() {
        return anAnnotation;
    }

    public void setAnAnnotation(boolean anAnnotation) {
        this.anAnnotation = anAnnotation;
    }

    public boolean isAnEnum() {
        return anEnum;
    }

    public void setAnEnum(boolean anEnum) {
        this.anEnum = anEnum;
    }


    public InitClass(Class clazz, String name, String simpleName, Method[] methods, Field[] declaredFields, boolean anInterface,boolean anAnnotation, boolean anEnum) {
        this.clazz = clazz;
        this.name = name;
        this.simpleName = simpleName;
        this.methods = methods;
        this.declaredFields = declaredFields;
        this.anInterface = anInterface;
        this.anAnnotation=anAnnotation;
        this.anEnum = anEnum;
    }


    private static List<Class> classesFromPackage = PackageUtil.getClasssFromPackage(rootPath);


    /**
     * @Author mabo
     * @Description   获取指定类路径下面的所有信息
     */
    public static List<InitClass> getInitAllClass(){
        //配置包的基本扫描路径
       List<InitClass> initClassList=new ArrayList<>();
        for (Class clazz : classesFromPackage) {

            //获取类名及其包名
            String name = clazz.getName();

            //获取类名
            String simpleName = clazz.getSimpleName();

            //获取实体的所有方法
            Method[] methods = clazz.getDeclaredMethods();

            //获取实体的所有属性
            Field[] declaredFields = clazz.getDeclaredFields();

            //是否为接口
            boolean anInterface = clazz.isInterface();

            //是否为注解
            boolean annotation = clazz.isAnnotation();

            //是否为枚举
            boolean anEnum = clazz.isEnum();
            initClassList.add(new InitClass(clazz,name, simpleName,  methods,declaredFields, anInterface,annotation, anEnum));
        }
        return initClassList;
    }

    /**
     * @Author mabo
     * @Description   获取所有扫描到的枚举
     */
    public static List<InitClass> getInitEnum(){
        List<InitClass> classList=new ArrayList<>();
        List<InitClass> initAllClass = getInitAllClass();
        for (InitClass clazz: initAllClass) {
            if (clazz.isAnEnum()){
                classList.add(clazz);
            }
        }
        return classList;
    }

    /**
     * @Author mabo
     * @Description   获取所有扫描到的接口
     */
    public static List<InitClass> getInitInterface(){
        List<InitClass> classList=new ArrayList<>();
        List<InitClass> initAllClass = getInitAllClass();
        for (InitClass clazz: initAllClass) {
            if (clazz.isAnInterface()&&!clazz.isAnAnnotation()){
                classList.add(clazz);
            }
        }
        return classList;
    }
    /**
     * @Author mabo
     * @Description   获取所有扫描到的接口
     */
    public static List<InitClass> getInitAnnotation(){
        List<InitClass> classList=new ArrayList<>();
        List<InitClass> initAllClass = getInitAllClass();
        for (InitClass clazz: initAllClass) {
            if (clazz.isAnAnnotation()){
                classList.add(clazz);
            }
        }
        return classList;
    }

    /**
     * @Author mabo
     * @Description   获取所有扫描到的类
     */
    public static List<InitClass> getInitClass(){
        List<InitClass> classList=new ArrayList<>();
        List<InitClass> initAllClass = getInitAllClass();
        for (InitClass clazz: initAllClass) {
            if (!clazz.isAnInterface()&&!clazz.isAnEnum()&&!clazz.isAnAnnotation()){
                classList.add(clazz);
            }
        }
        return classList;
    }
}
