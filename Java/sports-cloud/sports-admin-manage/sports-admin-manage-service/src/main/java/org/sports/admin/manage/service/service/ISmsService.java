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

package org.sports.admin.manage.service.service;

import org.sports.admin.manage.dao.entity.SmsDO;
import org.sports.admin.manage.service.vo.SmsVO;
import org.sports.admin.manage.service.vo.SmsVaVO;

import java.util.List;


public interface ISmsService {

    /**
     * 查询所有的短信配置
     * @return
     */
    SmsVO getSmsAllList(Boolean openStatus);
    /**
     * 保存所有的短信配置
     * @return
     */
    void createSms(SmsVO vo);

    /**
     * 获取验证方式
     * @return
     */
    List<SmsVaVO> getsmsVaList();

    /**
     * 获取配置规则能使用的短信配置
     * @return
     */
    List<SmsDO> getUseSmsDOList();

    /**
     * 短信验证码发送
     * @return
     */
    Boolean sendSmsMsg(String phone);

    /**
     * 短信异常处理通知解除
     */
    void smsErrorOff();
}
