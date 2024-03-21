package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.mapper.AppUserNoticeMapper;
import org.sports.admin.manage.service.service.AppUserNoticeService;
import org.sports.admin.manage.service.vo.AppUserNoticeVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.springframework.stereotype.Service;

/**
 * APP用户通知服务层实现

 */
@Service
public class AppUserNoticeServiceImpl extends ServiceImpl<AppUserNoticeMapper, AppUserNoticeDO> implements AppUserNoticeService {
    @Override
    public PageResponse<AppUserNoticeVO> getNoticeInfosByUserId(PageRequest pageRequest, Long userId) {
        LambdaQueryWrapper<AppUserNoticeDO> queryWrapper = Wrappers.lambdaQuery(AppUserNoticeDO.class)
                .eq(AppUserNoticeDO::getUserId, userId)
                .orderByDesc(AppUserNoticeDO::getCreateTime);
        Page<AppUserNoticeDO> appUserNoticeDOPage = this.baseMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(appUserNoticeDOPage, AppUserNoticeVO.class);
    }

    @Override
    public AppUserNoticeDO getLastInfoByUserId(Long userId) {
        return this.getOne(new LambdaQueryWrapper<AppUserNoticeDO>()
                .eq(AppUserNoticeDO::getUserId, userId)
                .orderByDesc(AppUserNoticeDO::getCreateTime)
                .last("limit 1"));
    }

    @Override
    public Long countNoRead(Long userId) {
        return this.count(new LambdaQueryWrapper<AppUserNoticeDO>()
                .eq(AppUserNoticeDO::getUserId, userId)
                .eq(AppUserNoticeDO::getReadFlag, false));
    }

    @Override
    public void readAllNoticeInfo(Long userId) {
         this.update(new LambdaUpdateWrapper<AppUserNoticeDO>()
                .eq(AppUserNoticeDO::getUserId, userId)
                .eq(AppUserNoticeDO::getReadFlag, false)
                .set(AppUserNoticeDO::getReadFlag, true));
    }

    @Override
    public void delNoticeByUserId(Long userId) {
        this.update(null, new LambdaUpdateWrapper<AppUserNoticeDO>()
                .eq(AppUserNoticeDO::getUserId, userId)
                .eq(AppUserNoticeDO::getDelFlag, 0)
                .set(AppUserNoticeDO::getDelFlag, 1));
    }
}
