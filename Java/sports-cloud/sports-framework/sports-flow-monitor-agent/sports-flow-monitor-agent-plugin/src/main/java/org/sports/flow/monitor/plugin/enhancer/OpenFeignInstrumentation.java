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

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.sports.flow.monitor.core.define.ClassEnhancePluginDefine;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.sports.flow.monitor.core.conf.Config.Agent.OPEN_FEIGN_ENHANCE_CLASS;

/**
 * OpenFeign 流量拦截
 *

 */
public final class OpenFeignInstrumentation implements ClassEnhancePluginDefine {
    
    private static final String ENHANCE_CLASS = OPEN_FEIGN_ENHANCE_CLASS;
    private static final String ENHANCE_METHOD = "execute";
    private static final String INTERCEPT_CLASS = "org.sports.flow.monitor.plugin.enhancer.OpenFeignInterceptor";
    
    @Override
    public ElementMatcher.Junction enhanceClass() {
        return named(ENHANCE_CLASS).and(not(isInterface()));
    }
    
    @Override
    public ElementMatcher<MethodDescription> getMethodsMatcher() {
        return named(ENHANCE_METHOD);
    }
    
    @Override
    public String getMethodsEnhancer() {
        return INTERCEPT_CLASS;
    }
}
