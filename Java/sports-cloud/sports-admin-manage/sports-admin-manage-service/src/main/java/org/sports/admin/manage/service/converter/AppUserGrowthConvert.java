package org.sports.admin.manage.service.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.sports.admin.manage.dao.entity.AppUserGrowthDO;
import org.sports.admin.manage.service.vo.AppUserGrowthVO;

/**
 * AppUserGrowthDO VS AppUserGrowthVO 转换器
 */
@Mapper
public interface AppUserGrowthConvert {
    AppUserGrowthConvert INSTANCE = Mappers.getMapper(AppUserGrowthConvert.class);

    AppUserGrowthDO convertToDo(AppUserGrowthVO bean);

    AppUserGrowthVO convertToVo(AppUserGrowthDO bean);
}
