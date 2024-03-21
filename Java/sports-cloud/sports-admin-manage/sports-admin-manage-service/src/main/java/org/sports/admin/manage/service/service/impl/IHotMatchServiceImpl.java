package org.sports.admin.manage.service.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.HotMatchDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.repository.IHotMatchDao;
import org.sports.admin.manage.dao.req.HotMatchAddRequest;
import org.sports.admin.manage.dao.req.HotMatchListRequest;
import org.sports.admin.manage.dao.req.MatchSearchVoRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.converter.HotMatchConvert;
import org.sports.admin.manage.service.service.IHotMatchService;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.admin.manage.service.vo.HotMatchListVo;
import org.sports.admin.manage.service.vo.MatchSearchVo;
import org.sports.admin.manage.service.vo.SdkCompetitionVo;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class IHotMatchServiceImpl implements IHotMatchService {
    private final IHotMatchDao dao;
    private final SdkService sdkService;

    @Override
    public List<MatchSearchVo> search(MatchSearchVoRequest request) {
        List<SdkCompetitionVo> sdkCompetitionVos = sdkService.searchCompetition(request.getCompetitionName(), request.getMatchType());
        if (CollectionUtils.isEmpty(sdkCompetitionVos)) {
            return Collections.emptyList();
        }
        return sdkCompetitionVos.stream().map(vo -> {
            MatchSearchVo searchVo = new MatchSearchVo();
            searchVo.setMatchType(request.getMatchType());
            searchVo.setCompetitionId(vo.getId());
            searchVo.setCompetitionName(vo.getShortNameZh());
            searchVo.setFullCompetitionName(vo.getNameZh());
            return searchVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HotMatchListVo> list(HotMatchListRequest request) {
        List<HotMatchDO> dos = dao.list(request);
        return HotMatchConvert.INSTANCE.toHotMatchListVoList(dos);
    }

    @Override
    public void add(HotMatchAddRequest request) {
        if (dao.exist(request.getCompetitionId(), request.getMatchType())) {
            throw new ServiceException("当前赛事已配置！");
        }
        SdkCompetitionVo competition= sdkService.findCompetitionById(request.getCompetitionId(), request.getMatchType());
        if(Objects.isNull(competition)){
            throw new ServiceException("当前赛事不存在！");
        }
        HotMatchDO hotMatchDO = HotMatchDO.builder()
                .id(SnowflakeIdUtil.nextId())
                .fullCompetitionName(competition.getNameZh())
                .competitionName(competition.getShortNameZh())
                .competitionId(request.getCompetitionId())
                .matchType(request.getMatchType()).build();
        dao.insert(hotMatchDO);
    }

    @Override
    public void remove(Long id) {
        dao.delete(id);
    }

    @Override
    @Cached(cacheType = CacheType.REMOTE, name = CacheConstant.HOT_COMPETITION_ID_LIST,key = "#matchType" , expire = 2, timeUnit = TimeUnit.MINUTES)
    public List<HotMatchDO> getHotCompetitionIdList(MatchType matchType) {
        HotMatchListRequest request = new HotMatchListRequest();
        request.setMatchType(matchType);
        return dao.list(request);
    }

}
