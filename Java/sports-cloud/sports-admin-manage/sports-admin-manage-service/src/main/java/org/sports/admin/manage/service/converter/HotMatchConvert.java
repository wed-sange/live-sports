package org.sports.admin.manage.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.sports.admin.manage.dao.entity.HotMatchDO;
import org.sports.admin.manage.dao.req.HotMatchAddRequest;
import org.sports.admin.manage.service.vo.HotMatchListVo;

import java.util.List;

@Mapper
public interface HotMatchConvert {
    HotMatchConvert INSTANCE = Mappers.getMapper(HotMatchConvert.class);

    HotMatchDO hotMatchAddRequestToDo(HotMatchAddRequest request);

    HotMatchListVo toHotMatchListVo(HotMatchDO hotMatchDO);

    List<HotMatchListVo> toHotMatchListVoList(List<HotMatchDO> dos);
}
