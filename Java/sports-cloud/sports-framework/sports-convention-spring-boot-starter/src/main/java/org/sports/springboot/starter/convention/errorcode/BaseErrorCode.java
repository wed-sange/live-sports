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

package org.sports.springboot.starter.convention.errorcode;

/**
 * 基础错误码定义
 *

 */
public enum BaseErrorCode implements IErrorCode {

    CLIENT_ERROR(777, "客户端远程调用错误"),
    UNAUTHORIZED(401, "用户未登录"),
    SERVICE_ERROR(500, "系统执行出错");

    
    private final int code;
    
    private final String message;
    
    BaseErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public int code() {
        return code;
    }
    
    @Override
    public String message() {
        return message;
    }
}
