package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.AppUserGrowthDO;
import org.sports.admin.manage.dao.mapper.AppUserGrowthMapper;
import org.sports.admin.manage.service.config.UserGrowthProperties;
import org.sports.admin.manage.service.converter.AppUserGrowthConvert;
import org.sports.admin.manage.service.enums.UserGrowthTypeEnum;
import org.sports.admin.manage.service.service.IAppUserGrowthService;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.vo.AppUserGrowthVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>
 * APP用户成长值信息 服务实现类
 * </p>
 *

 * @since 2023-07-26
 */
@Service
public class AppUserGrowthServiceImpl extends ServiceImpl<AppUserGrowthMapper, AppUserGrowthDO> implements IAppUserGrowthService {

    @Resource
    private IAppUserService appUserService;
    @Resource
    private UserGrowthProperties userGrowthProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserGrowthBatch(List<AppUserGrowthVO> userGrowthList){
        if(CollectionUtils.isEmpty(userGrowthList)){
            return;
        }
        List<AppUserGrowthDO> saveList = new ArrayList<>();
        AppUserGrowthDO appUserGrowthDO;
        Map<Long, Integer> userGrowthMap = new HashMap<>();
        for (AppUserGrowthVO appUserGrowthVO : userGrowthList) {
            appUserGrowthDO = AppUserGrowthConvert.INSTANCE.convertToDo(appUserGrowthVO);
            if(Objects.equals(appUserGrowthVO.getType(), UserGrowthTypeEnum.LIVE_CHAT.getType())){
                appUserGrowthDO.setEventGrowth(userGrowthProperties.getLiveValue() * appUserGrowthVO.getEventNumber());
            }else if(Objects.equals(appUserGrowthVO.getType(), UserGrowthTypeEnum.WATCH_LIVE.getType())){
                appUserGrowthDO.setEventGrowth(userGrowthProperties.getWatchValue() * appUserGrowthVO.getEventNumber());
            }else{
                continue;
            }
            appUserGrowthDO.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
            saveList.add(appUserGrowthDO);
            //统计每个用户需要调整的积分
            if(userGrowthMap.containsKey(appUserGrowthDO.getUserId())){
                userGrowthMap.put(appUserGrowthDO.getUserId(), userGrowthMap.get(appUserGrowthDO.getUserId()) + appUserGrowthDO.getEventGrowth());
            }else{
                userGrowthMap.put(appUserGrowthDO.getUserId(), appUserGrowthDO.getEventGrowth());
            }
        }
        this.saveBatch(saveList, 200);
        appUserService.addUserGrowthValueBatch(userGrowthMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserGrowthSingle(AppUserGrowthVO appUserGrowthVO){
        //判断用户是否存在
        if(Objects.isNull(appUserService.getById(appUserGrowthVO.getUserId()))){
            return;
        }
        //计算成长值 并保存成长记录
        AppUserGrowthDO appUserGrowthDO = AppUserGrowthConvert.INSTANCE.convertToDo(appUserGrowthVO);
        if(Objects.equals(appUserGrowthVO.getType(), UserGrowthTypeEnum.LIVE_CHAT.getType())){
            appUserGrowthDO.setEventGrowth(userGrowthProperties.getLiveValue() * appUserGrowthVO.getEventNumber());
        }else if(Objects.equals(appUserGrowthVO.getType(), UserGrowthTypeEnum.WATCH_LIVE.getType())){
            appUserGrowthDO.setEventGrowth(userGrowthProperties.getWatchValue() * appUserGrowthVO.getEventNumber());
        }else{
            return;
        }
        appUserGrowthDO.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
        this.save(appUserGrowthDO);
        //修改用户成长值
        appUserService.addUserGrowthValue(appUserGrowthDO.getUserId(), appUserGrowthDO.getEventGrowth());
    }

    @Override
    public LocalDateTime getLastDealEndTimeByLiveChat(){
        AppUserGrowthDO appUserGrowthDO = this.getOne(new QueryWrapper<AppUserGrowthDO>()
                .select("max(end_time) as end_time")
                .eq("type", UserGrowthTypeEnum.LIVE_CHAT.getType()));
        if(appUserGrowthDO == null){
            return null;
        }
        return appUserGrowthDO.getEndTime();
    }

}
