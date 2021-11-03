package com.mabo.framework.source.utils;

import java.util.Map;

public class MapUtil {
    public static String getMapString(Map<String,Object> map, String key) {
        if (map!=null){
            Object o = map.get(key);
            if (o!=null){
                return o.toString();
            }
            else
                return null;
        }
        else
            return null;

    }
}
