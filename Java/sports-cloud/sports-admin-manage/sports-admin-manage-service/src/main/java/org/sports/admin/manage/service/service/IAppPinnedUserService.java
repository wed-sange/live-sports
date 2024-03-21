
package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.AppPinnedUserDO;

import java.util.List;


public interface IAppPinnedUserService extends IService<AppPinnedUserDO> {


    List<AppPinnedUserDO> queryAnchorSetTopList(Long loginUserId);
    List<AppPinnedUserDO> queryUserSetTopList(Long loginUserId, Long anchorId);
}
