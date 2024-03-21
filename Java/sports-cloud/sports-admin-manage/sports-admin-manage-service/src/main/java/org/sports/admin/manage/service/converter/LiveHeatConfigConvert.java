package org.sports.admin.manage.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.sports.admin.manage.dao.entity.LiveHeatConfigDO;
import org.sports.admin.manage.service.vo.LiveHeatConfigVO;

/**
 * LiveHeatConfigDO VS LiveHeatConfigVO 转换器
 */
@Mapper
public interface LiveHeatConfigConvert{
    LiveHeatConfigConvert INSTANCE = Mappers.getMapper(LiveHeatConfigConvert.class);

    LiveHeatConfigDO convertToDo(LiveHeatConfigVO bean);

    LiveHeatConfigVO convertToVo(LiveHeatConfigDO bean);
}
