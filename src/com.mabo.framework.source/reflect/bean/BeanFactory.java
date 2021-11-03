package com.mabo.framework.source.reflect.bean;

import com.mabo.framework.source.annotation.aop.Aop;
import com.mabo.framework.source.annotation.bean.Bean;
import com.mabo.framework.source.annotation.bean.Parent;
import com.mabo.framework.source.annotation.bean.SetBean;
import com.mabo.framework.source.initClass.InitClass;
import com.mabo.framework.source.reflect.aop.CreateCglibBeanProxy;
import com.mabo.framework.source.reflect.quartz.QuartzReflect;
import com.mabo.framework.source.utils.Log;
import com.mabo.framework.source.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mabo
 * @Description   bean工厂（单例）
 * bean的扫描和初始化
 */
public class BeanFactory {
    //系统日志
    private static Log log=new Log(BeanFactory.class);
    //扫描所有的类和方法
    private static List<InitClass> initAllClass;
    //扫描获取所有的bean
    private static List<InitClass> allBeans=new ArrayList<>();
    //标注当前bean被有依赖未成功创建

    /**
     * @Author mabo
     * @Description   一级缓存
     * 生成所有实例化完成的bean
     */
    private static Map<String, Object>  firstBeans=new HashMap<>();

    /**
     * @Author mabo
     * @Description   二级缓存
     * 存放产生循环依赖的bean
     */
    private static Map<String, Object>  secondBeans=new HashMap<>();

    /**
     * @Author mabo
     * @Description   存放刚刚生成实例，未完全初始化的bean
     */
    private static Map<String, Object>  thirdBeans=new HashMap<>();

    /**
     * @Author mabo
     * @Description   beanFactory的初始化过程
     */

    static {
        //遍历类文件
        initAllClass = InitClass.getInitAllClass();
        //获取所有被Bean注解的列
        getInitAllBeans();
        //利用三级缓存初始化bean
        initBeans();
        //初始化定时器，并启动
        QuartzReflect.reflect();
    }

    /**
     * @Author mabo
     * @Description   获取bean的id
     */
    private static String getBeanValue(Class clazz){
        if (clazz.isAnnotationPresent(Bean.class)){
            //是否被Bean标注
            Bean bean = (Bean) clazz.getAnnotation(Bean.class);
            //获取被注解的bean的id  value
            String value = bean.value();
            if (value.equals("")){
                value = StringUtil.toLowerCaseFirstOne(clazz.getSimpleName());
            }
            return value;
        }
        return null;
    }

    /**
     * @Author mabo
     * @Description   三层缓存初始化bean
     */
    private static void initBeans(){
        for (InitClass  initClass:allBeans) {
            Class clazz = initClass.getClazz();
            String value = getBeanValue(clazz);
            //如果为空，需要初始化,否则跳过
            if (firstBeans.get(value)==null){
                //通过id初始化bean
                Object o = initBeanByValue(value);
                if (firstBeans.get(value)==null){
                    firstBeans.put(value,o);
                }
            }
        }
    }

    /**
     * @Author mabo
     * @Description 对需要进行增强bean经行增强
     */
    public static Object strongerBean(Object bean){
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(Aop.class)){
            Aop aop = clazz.getAnnotation(Aop.class);
            CreateCglibBeanProxy createBeanProxy=new CreateCglibBeanProxy();
            Object instance = createBeanProxy.getInstance(bean,aop);
            return instance;
        }
        return null;
    }
    /**
     * @Author mabo
     * @Description   根据value生成并返回bean实例
     */
    private static Object initBeanByValue(String value){
        for (InitClass  initClass:allBeans) {
            //该Class是一个类
            if (!initClass.isAnInterface() && !initClass.isAnEnum() && !initClass.isAnAnnotation()) {
                Class clazz = initClass.getClazz();
                Field[] fields = initClass.getDeclaredFields();
                Method[] methods = initClass.getMethods();
                String beanValue = getBeanValue(clazz);
                //找value到对应的bean
                if (beanValue.equals(value)){
                    //一级缓存中不存在bean,该bean需要初始化，开始
                    if (firstBeans.get(value)==null){
                        if (secondBeans.get(value)==null){
                            Object o1 = thirdBeans.get(value);
                            if (o1!=null){
                                //三级缓存中存在bean,一级不存在,说名该bean被循环依赖
                                //判断被循环的bean是否需要增强
                                //先注入，再增强
                                Object o = strongerBean(o1);
                                if (o!=null){
                                    secondBeans.put(value,o);
                                    return o;
                                }
                                else {
                                    return o1;
                                }

                            }
                            //三级缓存中都不存在该bean,去初始化该bean
                            else {
                                //根据class生成实例
                                Object object = createBeanByClass(clazz);
                                //先生成父类及获取其相关属性
                                getParentField(clazz,object);
                                //立刻写入三级缓存
                                //所有缓存都不存在该bean,向三级缓存写入bean
                                thirdBeans.put(value,object);
                                if (object!=null){
                                    //对bean注入属性
                                    for (Field field : fields) {
                                        //如果当前属性是一个bean,不是跳过
                                        if(field.isAnnotationPresent(SetBean.class)){
                                            SetBean annotation = field.getAnnotation(SetBean.class);
                                            String fieldId = annotation.value();
                                            //先判断一级级缓存是否存在
                                            if (firstBeans.get(fieldId)!=null){
                                                //存在，根据set方法，注入bean
                                                for (Method method:methods) {
                                                    if (method.getName().equals("set" + StringUtil.toUpperCaseFirstOne(fieldId))){
                                                        try {
                                                            method.invoke(object, firstBeans.get(fieldId));
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            log.error(method.getName()+"()方法执行失败");
                                                        }
                                                    }
                                                }
                                            }
                                            //一级缓存不存在,去根据id生成该bean,在进行set注入
                                            else{
                                                Object o = initBeanByValue(fieldId);
                                                if (secondBeans.get(fieldId)==null){
                                                    firstBeans.put(fieldId,o);
                                                }
                                                //存在，根据set方法，注入bean
                                                for (Method method:methods) {
                                                    if (method.getName().equals("set" + StringUtil.toUpperCaseFirstOne(fieldId))){
                                                        try {
                                                            method.invoke(object, o);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            log.error(method.getName()+"()方法执行失败");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //如果出现循环依赖，重新获取进行注入
                                    if (secondBeans.get(value)!=null){
                                        initBeanByValue(value);
                                    }
                                    //如果二级缓存不存在，再写入该bean
                                    if (secondBeans.get(value)==null){
                                        if (firstBeans.get(value)==null){
                                            Object o = strongerBean(object);
                                            if (o!=null){
                                                firstBeans.put(value,o);
                                            }
                                        }

                                    }
                                }
                                return object;
                            }
                        }
                        //二级缓存中存在改bean，去完成初始化
                        else{
                            Object object = secondBeans.get(value);
                            Field[] fields1 = object.getClass().getFields();
                            Method[] methods1 = object.getClass().getMethods();
                            if (object!=null){
                                //对bean注入属性
                                for (Field field : fields1) {
                                    //如果当前属性是一个bean,不是跳过
                                    if(field.isAnnotationPresent(SetBean.class)){
                                        SetBean annotation = field.getAnnotation(SetBean.class);
                                        String fieldId = annotation.value();
                                        //先判断一级级缓存是否存在
                                        if (firstBeans.get(fieldId)!=null){
                                            //存在，根据set方法，注入bean
                                            for (Method method:methods1) {
                                                if (method.getName().equals("set" + StringUtil.toUpperCaseFirstOne(fieldId))){
                                                    try {
                                                        method.invoke(object, firstBeans.get(fieldId));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        log.error(method.getName()+"()方法执行失败");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //对二级缓存进行注入属性
                            //注入后放到一级缓存
                            secondBeans.remove(value);
                            firstBeans.put(value,object);
                            return object;
                        }
                    }
                    //一级缓存中存在存在bean,该bean不需要初始化跳过
                    else {
                        Object o = firstBeans.get(value);
                        Object o1 = strongerBean(o);
                        if (o1!=null){
                            return o1;
                        }
                        else return o;

                    }
                }
            }
        }
        return null;
    }

    /**
     * @Author mabo
     * @Description   对父级bean进行初始化
     */
    private static void getParentField(Class clazz, Object son){
        //该类有继承,先实现父类
        Object parentObject=null;
        if(clazz.isAnnotationPresent(Parent.class)){
            Parent parent = (Parent) clazz.getAnnotation(Parent.class);
            //获取被注解的bean的id  value
            String parentValue = parent.value();
            parentObject = initBeanByValue(parentValue);
            firstBeans.put(parentValue,parentObject);

        }
        //获取父类的属性，给注入分类的属性子类
        //通过父类的set方法,给子类赋值
        //通过父类的get方法，获取属性值
        if (parentObject!=null){
            //获取子类的属性
            Field[] fields = clazz.getFields();
            //获取父类的方法
            Method[] methods = parentObject.getClass().getMethods();
            for (Field field: fields) {
                String fieldName = field.getName();
                Object getObject=null;
                //根据get方法，获取父类的属性值
                for (Method method:methods) {
                    if (method.getName().equals("get" + StringUtil.toUpperCaseFirstOne(fieldName))){
                        try {
                            //获取到父类的属性值
                            getObject = method.invoke(parentObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error(method.getName()+"()方法执行失败");
                        }
                    }
                }
                //如果获取到父类的属性值，调用父类的set方法给子类注入值
                if (getObject!=null){
                    for (Method method:methods) {
                        if (method.getName().equals("set" + StringUtil.toUpperCaseFirstOne(fieldName))){
                            try {
                                //调用父类的set方法对子类进行注入
                                method.invoke(son,getObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                                log.error(method.getName()+"()方法执行失败");
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * @Author mabo
     * @Description   根据class生成并返回bean实例
     */
    private static Object createBeanByClass(Class clazz){
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(clazz.getName()+"类生成错误,请检查是否有无参构造方法");
        }
        return o;
    }


    /**
     * @Author mabo
     * @Description   获取所有被bean标注的initClassBean
     */
    private static void getInitAllBeans(){
        HashMap<String, Boolean> map = new HashMap<>();
        for (InitClass  initClass:initAllClass) {
            if (!initClass.isAnInterface()&&!initClass.isAnEnum()&&!initClass.isAnAnnotation()){
                Class clazz = initClass.getClazz();
                if(clazz.isAnnotationPresent(Bean.class)){
                    Bean bean = (Bean) clazz.getAnnotation(Bean.class);
                    String value = bean.value();
                    if (value.equals("")){
                        value = StringUtil.toLowerCaseFirstOne(clazz.getSimpleName());
                    }
                    if (map.get(value)==null){
                        allBeans.add(initClass);
                        map.put(value,true);
                    }
                    else {
                        log.error("Bean"+clazz.getName()+"注入失败,原因是重复注入");
                        allBeans=null;
                    }
                }
            }
        }
    }
    /**
     * @Author mabo
     * @Description   根据id获取Bean
     */
    public static Object getBean(String value){
        Object o = firstBeans.get(value);
        return o;
    }
    /**
     * @Author mabo
     * @Description   根据id获取写入bean
     */
    public static void setBean(String value, Object object){
        firstBeans.put(value,object);
    }

}
