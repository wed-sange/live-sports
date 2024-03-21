package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.AppUserLiveShareDO;
import org.sports.admin.manage.dao.mapper.AppUserLiveShareMapper;
import org.sports.admin.manage.service.service.IAppUserLiveShareService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app用户直播分享记录服务实现

 */
@Service
public class AppUserLiveShareServiceImpl extends ServiceImpl<AppUserLiveShareMapper, AppUserLiveShareDO> implements IAppUserLiveShareService {

    @Override
    public Map<String, Integer> getLiveShareByGroupIds(List<String> liveIdList) {
        List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<AppUserLiveShareDO>()
                .select("live_id as liveId, count(user_id) as shareCount")
                .in("live_id", liveIdList)
                .groupBy("live_id"));
        Map<String, Integer> backMap = new HashMap<>();
        if(CollectionUtils.isEmpty(mapList)){
            return backMap;
        }
        for (Map<String, Object> map : mapList) {
            backMap.put(String.valueOf(map.get("liveId")), Integer.valueOf(String.valueOf(map.get("shareCount"))));
        }
        return backMap;
    }
}
