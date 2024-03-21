package org.sports.admin.manage.service.service;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.sports.admin.manage.dao.entity.HotMatchDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.HotMatchAddRequest;
import org.sports.admin.manage.dao.req.HotMatchListRequest;
import org.sports.admin.manage.dao.req.MatchSearchVoRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.vo.HotMatchListVo;
import org.sports.admin.manage.service.vo.MatchSearchVo;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IHotMatchService {
    List<MatchSearchVo> search(MatchSearchVoRequest request);

    List<HotMatchListVo> list(HotMatchListRequest request);

    void add(HotMatchAddRequest request);

    void remove(Long id);

    List<HotMatchDO> getHotCompetitionIdList(MatchType matchType);
}
