package org.sports.app.web.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryUpdatedListener;
import org.sports.admin.manage.dao.entity.SmsDO;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.service.ISmsService;
import org.sports.app.web.config.Sms4jConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class Sms4jListener {
    @Autowired
    Sms4jConfig sms4jConfig;

    @Resource
    private ISmsService smsService;

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        RTopic topic = redissonClient.getTopic(RedisCacheConstant.SMS_UPDATE_MSG_TOPC);

        topic.addListener(Long.class, (charSequence, status) -> {
            List<SmsDO> smsDOList = smsService.getUseSmsDOList();
            if (CollectionUtils.isNotEmpty(smsDOList)) {
                SmsBlend smsBlend = null;
                for (SmsDO smsDO : smsDOList) {
                    System.out.println("初始化短信"+smsDO.getId());
                    smsBlend = SmsFactory.getSmsBlend(smsDO.getId() + "");
                    // 创建SmsBlend 短信实例
                    if(Objects.isNull(smsBlend)){
                        SmsFactory.createSmsBlend(sms4jConfig, smsDO.getId() + "");
                    }else{
                        SmsFactory.reload(smsDO.getId() + "",sms4jConfig);
                    }
                }
            }
        });
    }

    @EventListener
    public void init(ApplicationContextEvent event) {
        List<SmsDO> smsDOList = smsService.getUseSmsDOList();
        if (CollectionUtils.isNotEmpty(smsDOList)) {
            for (SmsDO smsDO : smsDOList) {
                // 创建SmsBlend 短信实例
                SmsFactory.createSmsBlend(sms4jConfig, smsDO.getId() + "");
            }
        }
        //SmsFactory.createSmsBlend(sms4jConfig);
    }

}