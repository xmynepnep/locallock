package com.tzy.locallock.guavautil;

import com.google.common.cache.Cache;
import org.apache.commons.lang.StringUtils;

public class GuavaCacheUtil {

    /**
     * @Author: tanziyi
     * Description:
     * Date: 10:10 2020/8/5
     * Params:  设置cache的值如果不存在的话  不存在则设置成功 返回value  原本已经存在则返回null
     * @param cache
     * @param key
     * @param value
     */
    public static String setValueIfNotExist(Cache<String,String> cache , String key,String value){
        synchronized (cache){
            String result = cache.getIfPresent(key);
            if(StringUtils.isBlank(result)){
                cache.put(key,value);
                return value;
            }
            return null;
        }
    }
}

