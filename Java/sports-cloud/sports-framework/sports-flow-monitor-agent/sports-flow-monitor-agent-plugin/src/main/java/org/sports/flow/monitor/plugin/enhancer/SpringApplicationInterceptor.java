/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.flow.monitor.plugin.enhancer;

import org.sports.flow.monitor.core.define.InstanceMethodsAroundInterceptor;
import org.sports.flow.monitor.plugin.context.ApplicationContextHolderProxy;
import org.sports.flow.monitor.plugin.context.FlowMonitorVirtualUriLoader;
import org.sports.flow.monitor.plugin.hook.InitializingHookManager;
import org.sports.flow.monitor.plugin.writer.FlowMonitorWrite;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;

/**
 * SpringApplication 增强，获取 Spring 应用上下文
 *

 */
public final class SpringApplicationInterceptor implements InstanceMethodsAroundInterceptor {
    
    private final String APPLICATION_CONTEXT_CLASS_NAME = "org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext";
    
    @Override
    public void beforeMethod(Object obj, Method method, Object[] allArguments, Class<?>[] argumentsTypes) throws Throwable {
        
    }
    
    @Override
    public void afterMethod(Object obj, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object result, Throwable ex) throws Throwable {
        if (result != null && result.getClass().getName().equals(APPLICATION_CONTEXT_CLASS_NAME)) {
            ApplicationContextHolderProxy.initContext((ConfigurableApplicationContext) result);
            FlowMonitorVirtualUriLoader.loadConsumerUris();
            FlowMonitorVirtualUriLoader.loadProviderUris();
            FlowMonitorWrite.initScheduleWriteData();
            InitializingHookManager.INSTANCE.boot();
        }
    }
}
