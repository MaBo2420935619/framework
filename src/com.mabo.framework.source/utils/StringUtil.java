package com.mabo.framework.source.utils;

public class StringUtil {
    /**
     * @Author mabo
     * @Description   首字母转小写
     */

    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    /**
     * @Author mabo
     * @Description   首字母转大写
     */

    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * @Author mabo
     * @Description   大规模字符串合并来提升系统性能
     */
    public static String doAppend(String string1,String string2) {
        StringBuilder sb = new StringBuilder();
        sb.append(string1);
        sb.append(string2);
        return sb.toString();
    }
}
