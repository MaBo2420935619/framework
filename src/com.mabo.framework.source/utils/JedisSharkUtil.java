package com.mabo.framework.source.utils;

import redis.clients.jedis.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @Author mabo
 * @Description   集群部署redis使用改工具类
 */

public class JedisSharkUtil {
    private static   Log log=new Log(JedisSharkUtil.class);
    private static   PropertyUtil properties=new PropertyUtil();
    private static ShardedJedisPool shardedJedisPool;
    private static   String[] ips;
    private static String[] ports;
    private static int jedisMaxTotal;
    static {
        String jedisIp = properties.get("config/config.properties", "jedisIp");
        String jedisPort = properties.get("config/config.properties", "jedisPort");
        jedisMaxTotal=Integer.parseInt(properties.get("config/config.properties", "jedisMaxTotal"));
        ips = jedisIp.split("&");
        ports = jedisPort.split("&");
        log.info("shardedJedis初始化参数读取成功");
        //        log.info("JedisSharkUtil连接---------开始");
        String useRedis = properties.get("config/config.properties", "useRedis");
        if(useRedis.equals("true")){
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            try {
                for (int i=0;i< ips.length;i++) {
                    //获取连接对象jedis
                    try {
                        Jedis jedis=new Jedis(ips[i],Integer.parseInt(ports[i]));
                        jedis.flushAll();
                        jedis.flushDB();
                        jedis.close();
                        shards.add(new JedisShardInfo(ips[i],Integer.parseInt(ports[i])));
                        log.info(ips[i]+" : "+ports[i]+"    redis服务器读取并且初始化");
                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
                    }
                }
                if (shards.size()>0){
                    //配置连接池最大连接数量
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(jedisMaxTotal);
                    //创建分片式redis连接池
                    shardedJedisPool = new ShardedJedisPool(poolConfig, shards);

                }
            } catch (Exception e) {
//                e.printStackTrace();
                log.info("JedisSharkUtil连接---------失败");
            }
        }
    }
    public  Set<String> keys(String key){

        ShardedJedis shardedJedis = getShardedJedis();
        if (shardedJedis!=null){
            Set<String> hkeys=null;
            try {
               hkeys = shardedJedis.hkeys(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                shardedJedis.close();
                return hkeys;
            }
        }
        return null;
    }

    /**
     * @Author mabo
     * @Description   get
     */
    public String get(String key){
        ShardedJedis shardedJedis = getShardedJedis();
        if (shardedJedis!=null){
            String s=null;
            try {
               s = shardedJedis.get(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                shardedJedis.close();
                return s;
            }
        }
        return null;
    }
    /**
     * @Author mabo
     * @Description   set
     */
    public boolean set(String key,String value){
        ShardedJedis shardedJedis = getShardedJedis();
        if (shardedJedis!=null){
            try {
                shardedJedis.set(key, value);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                shardedJedis.close();
            }
        }
        return false;
    }

    /**
     * @Author mabo
     * @Description   判断当前key是否存在
     */
    public boolean exists(String key){
        ShardedJedis shardedJedis = getShardedJedis();
        if (shardedJedis!=null){
            Boolean exists=false;
            try {
                exists = shardedJedis.exists(key);

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                shardedJedis.close();
            }
            return exists;
        }
        return false;
    }

    /**
     * @Author mabo
     * @Description   删除当前key
     */
    public boolean del(String key){
        ShardedJedis shardedJedis = getShardedJedis();
        if (shardedJedis!=null){
            try {
                if (exists(key)) {
                    if (shardedJedis.del(key) == 1) {
                        //删除数据成功
                        shardedJedis.close();
//                        log.info(key+"删除数据成功");
                        return true;
                    } else {
                        shardedJedis.close();
//                        log.info(key+"删除数据成功");
                        return false;
                    }
                }
                else{
                    shardedJedis.close();
                    log.error(key+"不存在,删除失败");
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.error(key+"删除数据失败");
            }
        }
        log.error(key+"删除数据失败");
        try {
            shardedJedis.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("shardedJedis为空");
        }
        return false;
    }

    /**
     * @Author mabo
     * @Description   获取jedis连接对象
     */

    public ShardedJedis getShardedJedis(){
        try {
            //获取连接
            ShardedJedis jedis = shardedJedisPool.getResource();
            //序列化当前的集群
//                        log.info("JedisSharkUtil连接---------成功结束");
            return jedis;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("JedisSharkUtil连接---------失败");
        }
        return null;
    }


}
