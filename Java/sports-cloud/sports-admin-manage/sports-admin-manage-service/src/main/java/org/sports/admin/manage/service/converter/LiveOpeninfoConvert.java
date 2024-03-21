package org.sports.admin.manage.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sports.admin.manage.dao.entity.LiveOpeninfoDO;
import org.sports.admin.manage.service.vo.LiveOpeninfoVO;

import java.util.List;

/**
 * LiveOpeninfoDO VS LiveOpeninfoVO 转换器
 */
@Mapper
public interface LiveOpeninfoConvert {
    LiveOpeninfoConvert INSTANCE = Mappers.getMapper(LiveOpeninfoConvert.class);

    LiveOpeninfoDO convertToDo(LiveOpeninfoVO bean);

    LiveOpeninfoVO convertToVo(LiveOpeninfoDO bean);
    List<LiveOpeninfoVO> convertToVoList(List<LiveOpeninfoDO> beans);
}
