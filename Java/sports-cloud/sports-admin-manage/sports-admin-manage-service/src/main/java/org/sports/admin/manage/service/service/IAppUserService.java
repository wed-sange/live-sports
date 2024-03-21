package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.req.UserPageRequest;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * APP用户信息 服务类
 * </p>
 *

 * @since 2023-07-20
 */
public interface IAppUserService extends IService<AppUserDO> {



    /**
     * 根据手机号获取APP用户信息
     * @param tel
     * @return
     */
    AppUserDO getByTel(String tel);

    /**
     * 根据邮箱获取APP用户信息
     * @param email
     * @return
     */
    AppUserDO getByEmail(String email);

    @Transactional(rollbackFor = Exception.class)
    List<AppUserDO> getByNickName(String nickName);

    /**
     * 用户分页查询
     * @param pageRequest
     * @return
     */
    PageResponse<AppUserVO> getAppUserPage(UserPageRequest pageRequest);

    /**
     * 用户解禁
     * @param appUserVO
     */
    void unForbiddenUser(AppUserVO appUserVO);

    /**
     * 用户封禁
     * @param appUserDO
     */
    void forbiddenUser(AppUserDO appUserDO);

    /**
     * 批量添加用户成长值
     * @param userGrowthMap
     */
    void addUserGrowthValueBatch(Map<Long, Integer> userGrowthMap);

    /**
     * 单个添加用户成长值
     * @param userId
     * @param growthValue
     */
    void addUserGrowthValue(Long userId, Integer growthValue);

    /**
     * 根据用户ID 查询用户信息
     * @param id
     * @return
     */
    AppUserVO getCacheAppUserById(Long id);

    /**
     * 刷新app用户缓存
     * @param id
     */
    AppUserVO refreshCacheAppUser(Long id);

}
