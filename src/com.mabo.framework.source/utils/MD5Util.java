package com.mabo.framework.source.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @Author mabo
 * @Description   用于生成不可逆的MD5加密字符串
 */
public class MD5Util {
    /**
     * @Author mabo
     * @Description   用于生成不可逆的MD5加密字符串
     */
    public static String getKey(String string){
        MessageDigest m= null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            m.update(string.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte s[ ]=m.digest( );
        String result="";
        for (int i=0; i<s.length;i++){
            result+=Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
        }
        return  result;
    }
}
