package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.LiveHeatConfigDO;
import org.sports.admin.manage.dao.mapper.LiveHeatConfigMapper;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.converter.LiveHeatConfigConvert;
import org.sports.admin.manage.service.service.ILiveHeatConfigService;
import org.sports.admin.manage.service.vo.LiveHeatConfigVO;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 直播间热度管理 服务实现类
 * </p>
 *

 * @since 2023-07-26
 */
@Service
public class LiveHeatConfigServiceImpl extends ServiceImpl<LiveHeatConfigMapper, LiveHeatConfigDO> implements ILiveHeatConfigService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public LiveHeatConfigVO getLiveHeatConfig(){
        Object liveHeatConfig = redissonClient.getBucket(CacheConstant.ADMIN_CONFIG + "liveHeatConfig").get();
        if(liveHeatConfig != null){
            return (LiveHeatConfigVO)liveHeatConfig;
        }
        LiveHeatConfigDO liveHeatConfigDO = this.getById(1L);
        if(liveHeatConfigDO == null){
            //首次初始化
            liveHeatConfigDO = new LiveHeatConfigDO();
            liveHeatConfigDO.setId(1L);
            liveHeatConfigDO.setBaseHeat(100);
            liveHeatConfigDO.setBaseHeatHot(300);
            liveHeatConfigDO.setMsgSendRatio(1);
            liveHeatConfigDO.setShareRatio(1);
            liveHeatConfigDO.setPeopleNumberRatio(1);
            liveHeatConfigDO.setUpdateTime(LocalDateTime.now(ZoneOffset.UTC));
            Long userId = SotokenUtil.getUserId();
            liveHeatConfigDO.setUpdateBy(userId);
            this.save(liveHeatConfigDO);
        }
        LiveHeatConfigVO liveHeatConfigVO = LiveHeatConfigConvert.INSTANCE.convertToVo(liveHeatConfigDO);
        redissonClient.getBucket(CacheConstant.ADMIN_CONFIG + "liveHeatConfig").set(liveHeatConfigVO, 5, TimeUnit.MINUTES);
        return liveHeatConfigVO;
    }

    @Override
    public void updateLiveHeatConfig(LiveHeatConfigVO liveHeatConfigVO){
        Long userId = SotokenUtil.getCheckUserId();
        LiveHeatConfigDO liveHeatConfigDO = LiveHeatConfigConvert.INSTANCE.convertToDo(liveHeatConfigVO);
        liveHeatConfigDO.setId(1L);
        liveHeatConfigDO.setUpdateBy(userId);
        liveHeatConfigDO.setUpdateTime(LocalDateTime.now(ZoneOffset.UTC));
        this.updateById(liveHeatConfigDO);
        redissonClient.getBucket(CacheConstant.ADMIN_CONFIG + "liveHeatConfig").set(liveHeatConfigVO, 5, TimeUnit.MINUTES);
    }

}
