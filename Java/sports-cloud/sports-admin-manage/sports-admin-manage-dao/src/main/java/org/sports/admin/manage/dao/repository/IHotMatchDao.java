package org.sports.admin.manage.dao.repository;

import org.sports.admin.manage.dao.entity.HotMatchDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.HotMatchListRequest;

import java.util.List;

public interface IHotMatchDao {
    void insert(HotMatchDO hotMatchDO);

    void delete(Long id);

    List<HotMatchDO> list(HotMatchListRequest request);

    boolean exist(Integer competitionId, MatchType matchType);
}
