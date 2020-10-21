package com.tzy.locallock.guavautil;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GuavaCacheMap {

    private int defaultExpireTime = 5000;
    private int defaultMaxCount = 10000;
    private Map<String,Cache<String,String>> cacheMap = new HashMap();
    private Lock createLock = new ReentrantLock();

    public GuavaCacheMap(){

    }

    public Cache<String,String> getCache(String mapKey){
        Cache<String,String> result = cacheMap.get(mapKey);
        if(result == null){
            try {
                System.out.println(Thread.currentThread().getName()+"判断为空 准备创建");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            createLock.lock();
            boolean isExist = checkCacheIsExist(mapKey);
            if(isExist==false){
                System.out.println(Thread.currentThread().getName()+"创建成功");
                result = CacheBuilder.newBuilder().maximumSize(defaultMaxCount).expireAfterWrite(defaultExpireTime, TimeUnit.SECONDS).build();
                cacheMap.put(mapKey,result);
            }
            createLock.unlock();
        }
        result = cacheMap.get(mapKey);
        return result;
    }

    public boolean checkCacheIsExist(String mapKey){
        Cache<String,String> result = cacheMap.get(mapKey);
        if(result==null){
            System.out.println(Thread.currentThread().getName()+"判断为 "+false);
            return false;
        }
        System.out.println(Thread.currentThread().getName()+"判断为 "+true);
        return true;
    }

}

