package org.sports.springboot.starter.base.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor sendEmailThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,//核心数，一直都能工作的数量
                10,//请求处理大时，可以开放的最大工作数
                3,//开启最大工作数后，当无请求时，还让其存活的时间
                TimeUnit.SECONDS,//存活时间单位
                new LinkedBlockingDeque<>(50),//阻塞队列，保存操作请求线程
                Executors.defaultThreadFactory(),//创建线程的工厂类
                new ThreadPoolExecutor.AbortPolicy());//拒绝策略
        return threadPoolExecutor;
    }

}
