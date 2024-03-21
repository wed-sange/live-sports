package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.base.AppUserRegIdDO;

import java.util.List;


public interface IAppUserRegIdService extends IService<AppUserRegIdDO> {


    boolean bindRegId(Long checkUserId, String regId);

    List<AppUserRegIdDO> getUserRegIds(List<Long> userIds);

    boolean unbindRegId(Long checkUserId, String regId);
}
