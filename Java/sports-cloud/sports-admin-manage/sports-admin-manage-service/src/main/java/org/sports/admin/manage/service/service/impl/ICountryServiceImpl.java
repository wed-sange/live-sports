package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.CountryDO;
import org.sports.admin.manage.dao.mapper.CountryMapper;
import org.sports.admin.manage.service.service.ICountryService;
import org.springframework.stereotype.Service;


@Service
public class ICountryServiceImpl extends ServiceImpl<CountryMapper, CountryDO> implements ICountryService {


}
