package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.LiveOpeninfoDO;
import org.sports.admin.manage.dao.mapper.LiveOpeninfoMapper;
import org.sports.admin.manage.service.converter.LiveOpeninfoConvert;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.ILiveOpeninfoService;
import org.sports.admin.manage.service.vo.LiveOpeninfoVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * 直播开播信息服务实现类
 *

 * @since 2023-07-28
 */
@Service
public class LiveOpeninfoServiceImpl extends ServiceImpl<LiveOpeninfoMapper, LiveOpeninfoDO> implements ILiveOpeninfoService {

    @Override
    public LiveOpeninfoVO getUsedOpenInfoByLiveUserId(Long liveUserId){
        LambdaQueryWrapper<LiveOpeninfoDO> queryWrapper = Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                .eq(LiveOpeninfoDO::getUsed , YnEnum.ONE.getValue()).eq(LiveOpeninfoDO::getAnchorId, liveUserId);
        LiveOpeninfoDO liveOpeninfoDO = getOne(queryWrapper);
        if(liveOpeninfoDO == null){
            return null;
        }
        return LiveOpeninfoConvert.INSTANCE.convertToVo(liveOpeninfoDO);
    }

    @Override
    public LiveOpeninfoVO getOpenInfoById(Long openInfoId, Long anchorId) {
        LambdaQueryWrapper<LiveOpeninfoDO> queryWrapper = Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                .eq(LiveOpeninfoDO::getId , openInfoId).eq(LiveOpeninfoDO::getAnchorId , anchorId);
        LiveOpeninfoDO liveOpeninfoDO = getOne(queryWrapper);
        if(liveOpeninfoDO == null){
            return null;
        }
        return LiveOpeninfoConvert.INSTANCE.convertToVo(liveOpeninfoDO);
    }

    @Override
    public long countByAnchorId(Long anchorId) {
        LambdaQueryWrapper<LiveOpeninfoDO> queryWrapper = Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                .eq(LiveOpeninfoDO::getAnchorId , anchorId);
        return this.count(queryWrapper);
    }

    @Override
    public void delOpenInfoById(Long openInfoId, Long anchorId) {
        LambdaQueryWrapper<LiveOpeninfoDO> queryWrapper = Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                .eq(LiveOpeninfoDO::getId , openInfoId).eq(LiveOpeninfoDO::getAnchorId , anchorId);
        LiveOpeninfoDO liveOpeninfoDO = getOne(queryWrapper);
        if(Objects.nonNull(liveOpeninfoDO)){
            this.removeById(liveOpeninfoDO);
        }
    }

    @Override
    public List<LiveOpeninfoVO> getOpenInfoListByAnchorId(Long anchorId) {
        LambdaQueryWrapper<LiveOpeninfoDO> queryWrapper = Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                .eq(LiveOpeninfoDO::getAnchorId , anchorId).orderByDesc(LiveOpeninfoDO::getCreateTime);
        return LiveOpeninfoConvert.INSTANCE.convertToVoList(this.list(queryWrapper));
    }

    @Override
    public void setUsedOpenInfo(Long openInfoId, Long anchorId) {
        this.update(new LambdaUpdateWrapper<LiveOpeninfoDO>()
                .eq(LiveOpeninfoDO::getAnchorId, anchorId)
                .eq(LiveOpeninfoDO::getUsed, YnEnum.ONE.getValue())
                .set(LiveOpeninfoDO::getUsed, YnEnum.ZERO.getValue())
                .set(LiveOpeninfoDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        this.update(new LambdaUpdateWrapper<LiveOpeninfoDO>()
                .eq(LiveOpeninfoDO::getId, openInfoId)
                .eq(LiveOpeninfoDO::getAnchorId, anchorId)
                .set(LiveOpeninfoDO::getUsed, YnEnum.ONE.getValue())
                .set(LiveOpeninfoDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));

    }

    @Override
    public LiveOpeninfoVO getUsedOpenInfo(Long anchorId) {
        LambdaQueryWrapper<LiveOpeninfoDO> queryWrapper = Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                .eq(LiveOpeninfoDO::getAnchorId , anchorId).eq(LiveOpeninfoDO::getUsed, YnEnum.ONE.getValue()).last("LIMIT 1");
        LiveOpeninfoDO used = this.getOne(queryWrapper);
        if(Objects.isNull(used)){
            used  = this.getOne(Wrappers.lambdaQuery(LiveOpeninfoDO.class)
                    .eq(LiveOpeninfoDO::getAnchorId , anchorId).orderByDesc(LiveOpeninfoDO::getCreateTime).eq(LiveOpeninfoDO::getUsed, YnEnum.ONE.getValue()).last("LIMIT 1"));
        }
        return LiveOpeninfoConvert.INSTANCE.convertToVo(used);
    }
}
