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

package org.sports.springboot.starter.idempotent.core.token;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.ClientException;
import org.sports.springboot.starter.idempotent.config.IdempotentProperties;
import org.sports.springboot.starter.idempotent.core.AbstractIdempotentTemplate;
import org.sports.springboot.starter.idempotent.core.IdempotentParamWrapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

/**
 * 基于 Token 验证请求幂等性, 通常应用于 RestAPI 方法
 *

 */
@RequiredArgsConstructor
public final class IdempotentTokenExecuteHandler extends AbstractIdempotentTemplate implements org.sports.springboot.starter.idempotent.core.token.IdempotentTokenService {
    
    private final DistributedCache distributedCache;
    private final IdempotentProperties idempotentProperties;
    
    private static final String TOKEN_KEY = "idempotent-token";
    private static final String TOKEN_PREFIX_KEY = "idempotent:token:";
    private static final long TOKEN_EXPIRED_TIME = 6000;
    
    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        return new IdempotentParamWrapper();
    }
    
    @Override
    public String createToken() {
        String token = Optional.ofNullable(Strings.emptyToNull(idempotentProperties.getPrefix())).orElse(TOKEN_PREFIX_KEY) + UUID.randomUUID();
        distributedCache.put(token, "", Optional.ofNullable(idempotentProperties.getTimeout()).orElse(TOKEN_EXPIRED_TIME));
        return token;
    }
    
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader(TOKEN_KEY);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(TOKEN_KEY);
            if (StrUtil.isBlank(token)) {
                throw new ClientException(BaseErrorCode.CLIENT_ERROR);
            }
        }
        Boolean tokenDelFlag = distributedCache.delete(token);
        if (!tokenDelFlag) {
            String errMsg = StrUtil.isNotBlank(wrapper.getIdempotent().message())
                    ? wrapper.getIdempotent().message()
                    : BaseErrorCode.CLIENT_ERROR.message();
            throw new ClientException(errMsg, BaseErrorCode.CLIENT_ERROR);
        }
    }
}
