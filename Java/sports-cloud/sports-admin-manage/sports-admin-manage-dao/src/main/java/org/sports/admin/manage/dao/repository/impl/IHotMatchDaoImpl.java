package org.sports.admin.manage.dao.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.HotMatchDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.mapper.HotMatchMapper;
import org.sports.admin.manage.dao.repository.IHotMatchDao;
import org.sports.admin.manage.dao.req.HotMatchListRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class IHotMatchDaoImpl implements IHotMatchDao {
    private final HotMatchMapper mapper;

    @Override
    public void insert(HotMatchDO hotMatchDO) {
        mapper.insert(hotMatchDO);
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<HotMatchDO> list(HotMatchListRequest request) {
        return mapper.selectList(Wrappers.lambdaQuery(HotMatchDO.class)
                .eq(Objects.nonNull(request.getMatchType()), HotMatchDO::getMatchType, request.getMatchType())
        );
    }

    @Override
    public boolean exist(Integer competitionId, MatchType matchType) {
        return !mapper.selectList(Wrappers.lambdaQuery(HotMatchDO.class)
                .eq(Objects.nonNull(competitionId), HotMatchDO::getCompetitionId, competitionId)
                .eq(Objects.nonNull(matchType), HotMatchDO::getMatchType, matchType)
        ).isEmpty();
    }
}
