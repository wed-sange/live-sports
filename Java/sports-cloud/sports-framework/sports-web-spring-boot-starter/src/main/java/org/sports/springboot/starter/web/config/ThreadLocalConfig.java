package org.sports.springboot.starter.web.config;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ThreadLocalConfig {

    private static ThreadLocal<Map<String, Object>> mapThreadLocal = new ThreadLocal<>();

    //获取当前线程的存的变量
    public Object get(String key) {
        Map<String,Object> map = mapThreadLocal.get();
        if(CollectionUtil.isEmpty(map)){
            return null;
        }
        return map.get(key);
    }

    //设置当前线程的存的变量
    public void set(String key, Object object) {
        Map<String,Object> map = mapThreadLocal.get();
        if(CollectionUtil.isEmpty(map)){
            map = new HashMap<>();
        }
        map.put(key, object);
        mapThreadLocal.set(map);
    }

    //移除当前线程的存的变量
    public void remove() {
        mapThreadLocal.remove();
    }

}
