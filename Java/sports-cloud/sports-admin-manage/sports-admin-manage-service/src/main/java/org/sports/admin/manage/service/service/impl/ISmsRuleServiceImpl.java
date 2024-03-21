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

package org.sports.admin.manage.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.SmsRuleDO;
import org.sports.admin.manage.dao.mapper.SmsRuleMapper;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.service.ISmsRuleService;
import org.sports.admin.manage.service.vo.SmsRuleVO;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ISmsRuleServiceImpl extends ServiceImpl<SmsRuleMapper, SmsRuleDO> implements ISmsRuleService {

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private SmsRuleMapper smsRuleMapper;

    /**
     * 查询配置
     * @return
     */
    @Override
    public SmsRuleVO getSmsRuleInfo() {
        List<SmsRuleDO> smsRuleDOList = smsRuleMapper.selectList(Wrappers.<SmsRuleDO>lambdaQuery());
        if(CollectionUtils.isNotEmpty(smsRuleDOList)){
            SmsRuleDO smsRuleDO = smsRuleDOList.get(0);
            SmsRuleVO smsRuleVO = BeanUtil.convert(smsRuleDO,SmsRuleVO.class);
            if(Objects.nonNull(smsRuleDO.getVaType())){
                smsRuleVO.setVaTypeList(JSON.parseArray(smsRuleDO.getVaType(),Long.class));
            }
            return smsRuleVO;
        }
        return null;
    }

    @Override
    public void createSmsRule(SmsRuleVO vo) {
        List<SmsRuleDO> smsRuleDOList = smsRuleMapper.selectList(Wrappers.<SmsRuleDO>lambdaQuery());
        List<Long> vaTypeList = vo.getVaTypeList();
        if(CollectionUtils.isNotEmpty(smsRuleDOList)){
            SmsRuleDO smsRuleDO = smsRuleDOList.get(0);
            SmsRuleDO newSmsRuleDO = BeanUtil.convert(vo,SmsRuleDO.class);
            newSmsRuleDO.setId(smsRuleDO.getId());
            newSmsRuleDO.setVaType(JSON.toJSONString(vaTypeList));
            smsRuleMapper.updateById(newSmsRuleDO);
        }else{
            SmsRuleDO newSmsRuleDO = BeanUtil.convert(vo,SmsRuleDO.class);
            newSmsRuleDO.setVaType(JSON.toJSONString(vaTypeList));
            smsRuleMapper.insert(newSmsRuleDO);
        }
        redissonClient.getBucket(CacheConstant.APP_SMS_ERROR_COUNT).set(0);
        //订阅更新
        RTopic topic = redissonClient.getTopic(RedisCacheConstant.SMS_UPDATE_MSG_TOPC);
        topic.publish(System.currentTimeMillis());
    }
}
