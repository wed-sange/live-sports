package org.sports.springboot.starter.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private ThreadLocalConfig threadLocalConfig;
    /**
     * @return 返回 true 放行、放回 false 拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        threadLocalConfig.set("reqUrl", request.getRequestURL());
        return true;
    }


}
