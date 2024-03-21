package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.base.AppUserRegIdDO;
import org.sports.admin.manage.dao.mapper.AppUserRegIdMapper;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class IAppUserRegIdServiceImpl extends ServiceImpl<AppUserRegIdMapper, AppUserRegIdDO> implements IAppUserRegIdService {


    @Override
    @Transactional
    public boolean bindRegId(Long checkUserId, String regId) {
        //先删除
        this.remove(Wrappers.lambdaQuery(AppUserRegIdDO.class).eq(AppUserRegIdDO::getRegId, regId));
        AppUserRegIdDO one = this.getOne(Wrappers.lambdaQuery(AppUserRegIdDO.class).eq(AppUserRegIdDO::getUserId, checkUserId).eq(AppUserRegIdDO::getRegId, regId));
        if(Objects.nonNull(one)){
            return true;
        }
        return this.save(AppUserRegIdDO.builder().id(SnowflakeIdUtil.nextId()).userId(checkUserId).regId(regId).build());
    }

    @Override
    public List<AppUserRegIdDO> getUserRegIds(List<Long> userIds) {
        return this.list(Wrappers.lambdaQuery(AppUserRegIdDO.class).in(AppUserRegIdDO::getUserId, userIds));
    }

    @Override
    public boolean unbindRegId(Long checkUserId, String regId) {
        AppUserRegIdDO one = this.getOne(Wrappers.lambdaQuery(AppUserRegIdDO.class).eq(AppUserRegIdDO::getUserId, checkUserId).eq(AppUserRegIdDO::getRegId, regId));
        if(Objects.isNull(one)){
            return true;
        }
        return this.removeById(one);
    }
}
