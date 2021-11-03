package com.mabo.framework.source.utils;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * @Author mabo
 * @Description   用于读取配置文件的工具类
 */

public class PropertyUtil {
    //此处配置项目文件读取的根路径，轻易不要修改
    private static String rootPath="/src/";
    //设置配置文件的基本路径
    private  static String path = System.getProperty("user.dir");
    private  static BufferedReader bufferedReader;
    private  static Properties properties = new Properties();

    /**
     * @Author mabo
     * @Description   指定配置文件名,和key，获取value
     */
    public static String get(String propertyFileName, String key){

        // 使用InPutStream流读取properties文件
        String file = null;
        try {
            file=path+rootPath+propertyFileName;
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(file+"文件不存在");
        }
        try {
            properties.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(file+"文件读取失败");
        }
        String value=null;
        value =  properties.getProperty(key);
        if (value==null){
            System.out.println("配置文件"+propertyFileName+"获取-------:"+key+"失败");
        }
//        try {
//            if (bufferedReader!=null){
//                bufferedReader.close();
//            }
//        } catch (IOException e) {
//        }
        return  value;
    }

    /**
     * @Author mabo
     * @Description  根据配置文件传入的map组装数据
     */
    public static String get(String propertyFileName, String key, Map<String, String> map){
        try {
            String string = get(propertyFileName, key);
            for(int i = 0;  i < string.length(); i ++) {
                int flag = string.indexOf("$",i);
                int left = string.indexOf("{",i);
                int right = string.indexOf("}", i);
                if(flag < 0||left < 0||right < 0) {
                    System.out.println(propertyFileName+"配置文件读取错误");
                    return null;
                }
                else {
                    String value = string.substring(left+1, right);
                    String s = map.get(value);
                    string=  string.replace("${"+value+"}",s);
                }
                i = right;
            }
            return string;
        } catch (Exception e) {
            System.out.println(propertyFileName+"配置文件读取错误");
            return null;
        }
    }

}
