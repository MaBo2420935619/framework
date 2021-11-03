package com.mabo.framework.source.utils;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author mabo
 * @Description   日志记录工具类
 */

public class Log {
    private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat ymdHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static  String logPosition=System.getProperty("user.dir");
    public static Class aClass;

    public Log(Class clazz) {
        this.aClass=clazz;
    }

    /**
     * @Author mabo
     * @Description   用于接受消息
     */
    public void message(String info,int flag){
        String messageType="";
        if(flag==1){
            messageType="     info:";
        }
        else if(flag==0){
            messageType="     error:";
        }
        else if(flag==-1){
            messageType="     warning:";
        }
        Date date=new Date();
        StringBuilder msg=new StringBuilder();
        msg.append(logPosition);
        msg.append("\\log\\");
        msg.append(ymd.format(date));
        msg.append(".txt");
        File file = new File(msg.toString());
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OutputStreamWriter write = null;
        try {
            write = new OutputStreamWriter(new FileOutputStream(file,true),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter writer=new BufferedWriter(write);
        //写入信息
        try {
            writer.append(aClass.getName()+ messageType+ymdHMS.format(date)+"::"+info);
            System.out.println(aClass.getName()+ messageType+ymdHMS.format(date)+"::"+info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author mabo
     * @Description   信息日志
     */
    public void info(String info){
        Log logUtil=new Log(aClass);
        logUtil.message(info,1);
    }
    /**
     * @Author mabo
     * @Description   错误日志
     */
    public void error(String error) {
        Log logUtil=new Log(aClass);
        logUtil.message(error,0);
    }

    /**
     * @Author mabo
     * @Description   警告日志
     */
    public void warning(String info){
        Log logUtil=new Log(aClass);
        logUtil.message(info,-1);
    }
}
