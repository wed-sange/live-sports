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

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.SmsDO;
import org.sports.admin.manage.dao.entity.SmsRuleDO;
import org.sports.admin.manage.dao.mapper.SmsMapper;
import org.sports.admin.manage.dao.mapper.SmsRuleMapper;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.constant.SMSConstant;
import org.sports.admin.manage.service.service.ISmsService;
import org.sports.admin.manage.service.vo.SmsVO;
import org.sports.admin.manage.service.vo.SmsVaVO;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.satoken.constant.SotokenConstant;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ISmsServiceImpl extends ServiceImpl<SmsMapper, SmsDO> implements ISmsService {

    @Resource
    private SmsMapper smsMapper;

    @Resource
    private SmsRuleMapper smsRuleMapper;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 查询所有的短信配置
     *
     * @return
     */
    @Override
    public SmsVO getSmsAllList(Boolean openStatus) {
        List<SmsDO> smsDOList = smsMapper.selectList(Wrappers.<SmsDO>lambdaQuery()
                .eq(Objects.nonNull(openStatus), SmsDO::getOpenStatus, openStatus));
        return SmsVO.builder().smsDOList(getSortSmsDOList(smsDOList)).build();
    }

    public List<SmsDO> getSortSmsDOList(List<SmsDO> smsDOList) {
        List<SmsDO> newNmsDOList = Lists.newArrayList();
        Map<Integer, List<SmsDO>> map = smsDOList.stream().collect(Collectors.groupingBy(SmsDO::getSupplier));
        for (Integer key : map.keySet()) {
            List<SmsDO> smsDOListKey = map.get(key);
            Collections.sort(smsDOListKey, Comparator.comparingInt(SmsDO::getSupplier));
            newNmsDOList.addAll(smsDOListKey);
        }
        return newNmsDOList;
    }

    /**
     * 保存sms
     *
     * @param vo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createSms(SmsVO vo) {
        if (Objects.isNull(vo) || CollectionUtils.isEmpty(vo.getSmsDOList())) {
            return;
        }
        List<SmsDO> smsDOList = vo.getSmsDOList();

        //需要删除的
        List<SmsDO> allSmsDOList = smsMapper.selectList(Wrappers.<SmsDO>lambdaQuery());
        if (CollectionUtils.isNotEmpty(allSmsDOList)) {
            List<Long> allSmsIds = smsDOList.stream().map(SmsDO::getId).collect(Collectors.toList());
            List<SmsDO> deleteSmsDOList = allSmsDOList.stream()
                    .filter(smsDO -> !allSmsIds.contains(smsDO.getId())).collect(Collectors.toList());
            //判断功能配置是否已经设置
            check(deleteSmsDOList);
            if (CollectionUtils.isNotEmpty(deleteSmsDOList)) {
                smsMapper.deleteBatchIds(deleteSmsDOList.stream().map(SmsDO::getId).collect(Collectors.toList()));
            }
        }

        //设置编号
        smsDOList = getSmsDOList(smsDOList);

        //新增的
        List<SmsDO> newSmsDOList = smsDOList.stream()
                .filter(smsDO -> Objects.isNull(smsDO.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newSmsDOList)) {
            newSmsDOList.forEach(smsDO -> {
                smsMapper.insert(smsDO);
            });
        }
        //更新的
        List<SmsDO> updateSmsDOList = smsDOList.stream()
                .filter(smsDO -> Objects.nonNull(smsDO.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updateSmsDOList)) {
            //判断是否有关闭状态的
            List<SmsDO> onSmsDOList = updateSmsDOList.stream()
                    .filter(smsDO -> !smsDO.getOpenStatus()).collect(Collectors.toList());
            //判断功能配置是否已经设置
            check(onSmsDOList);
            updateSmsDOList.forEach(smsDO -> {
                smsMapper.updateById(smsDO);
            });
        }

        redissonClient.getBucket(CacheConstant.APP_SMS_ERROR_COUNT).set(0);
        //订阅更新
        RTopic topic = redissonClient.getTopic(RedisCacheConstant.SMS_UPDATE_MSG_TOPC);
        topic.publish(System.currentTimeMillis());
    }

    /**
     * 校验功能配置是否已经设置
     *
     * @param smsDOList
     */
    public void check(List<SmsDO> smsDOList) {
        //查询配置规则
        List<SmsRuleDO> smsRuleDOList = smsRuleMapper.selectList(Wrappers.<SmsRuleDO>lambdaQuery());
        //判断功能配置是否已经设置
        if (CollectionUtils.isNotEmpty(smsDOList) && CollectionUtils.isNotEmpty(smsRuleDOList)) {
            List<Long> vaTypeList = JSON.parseArray(smsRuleDOList.get(0).getVaType(), Long.class);
            List<SmsDO> onSmsDOList1 = smsDOList.stream()
                    .filter(onSmsDO -> vaTypeList.contains(onSmsDO.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(onSmsDOList1)) {
                throw new ServiceException("请先删除短信功能配置选择验证");
            }
        }
    }

    @Override
    public List<SmsVaVO> getsmsVaList() {
        SmsVO smsVO = getSmsAllList(true);
        if (Objects.isNull(smsVO) && CollectionUtils.isEmpty(smsVO.getSmsDOList())) {
            return Collections.EMPTY_LIST;
        }
        return BeanUtil.convert(smsVO.getSmsDOList(), SmsVaVO.class);
    }

    /**
     * 设置编号
     *
     * @param smsDOList
     * @return
     */
    public List<SmsDO> getSmsDOList(List<SmsDO> smsDOList) {
        List<SmsDO> newSmsDOList = Lists.newArrayList();
        Map<Integer, List<SmsDO>> map = smsDOList.stream().collect(Collectors.groupingBy(SmsDO::getSupplier));
        for (Integer key : map.keySet()) {
            List<SmsDO> smsDOListKey = map.get(key);
            List<Integer> smsNoList = smsDOListKey.stream().map(SmsDO::getSmsNo).filter(Objects::nonNull).collect(Collectors.toList());
            int maxSmsNo = 0;
            if (CollectionUtils.isNotEmpty(smsNoList)) {
                maxSmsNo = smsNoList.stream().mapToInt(Integer::intValue).max().orElse(0);
            }
            for (SmsDO smsDO : smsDOListKey) {
                if(Objects.equals(smsDO.getSmsNo(),maxSmsNo)){
                    newSmsDOList.add(smsDO);
                    continue;
                }
                maxSmsNo++;
                smsDO.setSmsNo(maxSmsNo);
                newSmsDOList.add(smsDO);
            }
        }
        return newSmsDOList;
    }

    /**
     * 获取配置使用的短信配置
     *
     * @return
     */
    @Override
    public List<SmsDO> getUseSmsDOList() {
        List<SmsRuleDO> smsRuleDOList = smsRuleMapper.selectList(Wrappers.<SmsRuleDO>lambdaQuery());
        if (CollectionUtils.isEmpty(smsRuleDOList)  || Objects.isNull(smsRuleDOList.get(0).getVaType())) {
            return Collections.EMPTY_LIST;
        }
        List<Long> vaTypeList = JSON.parseArray(smsRuleDOList.get(0).getVaType(), Long.class);
        if (CollectionUtils.isNotEmpty(vaTypeList)) {
            List<SmsDO> smsDOList = smsMapper.selectList(Wrappers.<SmsDO>lambdaQuery().in(SmsDO::getId, vaTypeList));
            return smsDOList;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 给管理员发送异常短信验证码
     *
     * @param phone
     * @return
     */
    public void sendSMSError(List<Long> vaTypeList, Long configId,SmsRuleDO smsRuleDO,String phone) {
        Integer errorNum = (Integer) redissonClient.getBucket(CacheConstant.APP_SMS_ERROR_COUNT).get();
        if(smsRuleDO.getErrorNum()>errorNum){
            return ;
        }
        //获取下一个厂商发送
        configId = getNextNum(vaTypeList,  configId);
        SmsDO smsDO = smsMapper.selectById(configId);
        String msg = Objects.equals(smsDO.getSupplier(), SMSConstant.SUPPLIER_ALI)?"阿里云":"腾讯云"+smsDO.getSmsNo()+"异常次数达到上限";
        SmsBlend smsBlend = SmsFactory.getSmsBlend(configId + "");
        SmsResponse smsResponse = smsBlend.sendMessage(phone, msg);
//        if(!smsResponse.isSuccess()){
//        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @param msg
     * @return
     */
    @Override
    public Boolean sendSmsMsg(String phone) {
        //阿里云和腾讯云区号加入发不了消息，暂时不加，先预留
        String bucketKey = SotokenConstant.TOKEN_KAPTCHA_PRE + UserChannelEnum.APP.getValue() + ":" + phone;
        String code = (String) redissonClient.getBucket(bucketKey).get();
        if (StringUtils.isNotBlank(code)) {
            return true;
        }
        code = RandomUtil.randomNumbers(6);
        //设置短信有效期 5分钟有效
        Integer time = 5;

        Long configId = null;
        List<Long> vaTypeList = null;
        SmsRuleDO smsRuleDO = null;
                //查询配置规则
        List<SmsRuleDO> smsRuleDOList = smsRuleMapper.selectList(Wrappers.<SmsRuleDO>lambdaQuery());
        //判断功能配置是否已经设置
        if (CollectionUtils.isNotEmpty(smsRuleDOList)) {
            smsRuleDO = smsRuleDOList.get(0);
            if (Objects.isNull(smsRuleDO.getVaType())) {
                throw new ServiceException("请配置短信验证方式");
            }
            vaTypeList = JSON.parseArray(smsRuleDO.getVaType(), Long.class);
            if (CollectionUtils.isEmpty(vaTypeList)) {
                throw new ServiceException("请配置短信验证方式");
            }
            //1随机使用 2轮替使用
            if (Objects.equals(smsRuleDO.getUseType(), SMSConstant.USETYPE1)) {
                //随机使用
                configId = getRandom(vaTypeList);
            }
            if (Objects.equals(smsRuleDO.getUseType(), SMSConstant.USETYPE2)) {
                //轮替使用
                configId = getRotation(vaTypeList, smsRuleDO);
            }
        }
        if (Objects.nonNull(configId)) {
            SmsBlend smsBlend = SmsFactory.getSmsBlend(configId + "");
            LinkedHashMap<String, String> map = new LinkedHashMap();
            map.put("code",code);
            map.put("time",time+"");
            SmsResponse smsResponse = smsBlend.sendMessage(phone, map);
            if(!smsResponse.isSuccess()){
                //发送失败异常统计
                Integer errorNum = (Integer) redissonClient.getBucket(CacheConstant.APP_SMS_ERROR_COUNT).get();
                redissonClient.getBucket(CacheConstant.APP_SMS_ERROR_COUNT).set(Objects.isNull(errorNum)?1:errorNum+1);
                sendSMSError(vaTypeList,configId,smsRuleDO,phone);
            }
            Integer errorNum = (Integer) redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID + ":" + configId).get();
            redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID + ":" + configId).set(Objects.isNull(errorNum)?1:errorNum+1);
        }else{
            throw new ServiceException("请配置短信验证方式");
        }
        //保存5分钟
        redissonClient.getBucket(bucketKey).set(code, time, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void smsErrorOff() {
        redissonClient.getBucket(CacheConstant.APP_SMS_ERROR_COUNT).set(0);
    }


    public Long getRotation(List<Long> vaTypeList, SmsRuleDO smsRuleDO) {
        //获取正在发送的厂商ID
        Long vaTypeId = (Long) redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID).get();
        if (Objects.isNull(vaTypeId)) {
            vaTypeId = vaTypeList.get(0);
            redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID).set(vaTypeId);
            return vaTypeId;
        }
        //判断vaTypeId是否在vaTypeList中
        if (vaTypeList.contains(vaTypeId)) {
            //判断是否已经发送上限了
            Long sendCount = (Long) redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID + ":" + vaTypeId).get();
            sendCount = Objects.isNull(sendCount) ? 0 : sendCount;
            if (smsRuleDO.getRatateNum() <= sendCount) {
                Long nextNum = getNextNum(vaTypeList, vaTypeId);
                redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID).set(nextNum);
                redissonClient.getBucket(CacheConstant.APP_SMS_CONFIGID + ":" + vaTypeId).delete();
                return nextNum;
            }
        }
        return vaTypeId;
    }

    /**
     * 获取下一个轮替数字
     *
     * @param vaTypeList
     * @return
     */
    public Long getNextNum(List<Long> vaTypeList, Long num) {
        Long nextNum = null;
        if (vaTypeList.contains(num)) {
            int index = vaTypeList.indexOf(num);
            if (index < vaTypeList.size() - 1) {
                nextNum = vaTypeList.get(index + 1);
            } else {
                nextNum = vaTypeList.get(0);
            }
        } else {
            for (long i : vaTypeList) {
                if (i > num) {
                    nextNum = i;
                    break;
                }
            }
            if (Objects.isNull(nextNum)) {
                nextNum = vaTypeList.get(0);
            }
        }
        return nextNum;
    }

    /**
     * 随机短信厂商
     *
     * @param vaTypeList
     * @return
     */
    public Long getRandom(List<Long> vaTypeList) {
        Long vaTypeId = vaTypeList.stream()
                .skip(new Random().nextInt(vaTypeList.size()))
                .findFirst()
                .orElse(null);
        return vaTypeId;
    }


}
