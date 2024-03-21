package org.sports.admin.manage.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.req.LiveOpenRequest;


@Mapper
public interface LiveConvert {
    LiveConvert INSTANCE = Mappers.getMapper(LiveConvert.class);

    LiveDO liveOpenRequestToDo(LiveOpenRequest request);
}
