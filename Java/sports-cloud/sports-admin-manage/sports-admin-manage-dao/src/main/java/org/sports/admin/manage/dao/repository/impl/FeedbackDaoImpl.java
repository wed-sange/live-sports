package org.sports.admin.manage.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.FeedbackDO;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.admin.manage.dao.mapper.FeedbackMapper;
import org.sports.admin.manage.dao.repository.IFeedbackDao;
import org.sports.admin.manage.dao.req.FeedbackListByAdminPageRequest;
import org.sports.admin.manage.dao.req.FeedbackListByUserRequest;
import org.sports.springboot.starter.common.enums.DelEnum;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackDaoImpl implements IFeedbackDao {
    private final FeedbackMapper mapper;

    @Override
    public Page<FeedbackDO> page(Long userId, FeedbackListByUserRequest pageRequest) {
        LambdaQueryWrapper<FeedbackDO> queryWrapper = Wrappers.lambdaQuery(FeedbackDO.class)
                .eq(FeedbackDO::getFeedbackUserId, userId);

        return mapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
    }

    @Override
    public PageResponse<FeedbackDO> page(FeedbackListByAdminPageRequest pageRequest) {
        LambdaQueryWrapper<FeedbackDO> queryWrapper = Wrappers.lambdaQuery(FeedbackDO.class);
        FeedbackStatus feedbackStatus = pageRequest.getFeedbackStatus();
        queryWrapper.eq(Objects.nonNull(feedbackStatus), FeedbackDO::getFeedbackStatus, feedbackStatus);
        queryWrapper.eq(Objects.nonNull(pageRequest.getFeedbackUserId()), FeedbackDO::getFeedbackUserId, pageRequest.getFeedbackUserId());
        queryWrapper.eq(Objects.nonNull(pageRequest.getFeedbackType()), FeedbackDO::getFeedbackType, pageRequest.getFeedbackType());
        queryWrapper.eq(Objects.nonNull(pageRequest.getFeedbackUserName()), FeedbackDO::getFeedbackUserName, pageRequest.getFeedbackUserName());
        queryWrapper.orderByDesc(FeedbackDO::getFeedbackTime);
        Page<FeedbackDO> FeedbackDOPage = mapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(FeedbackDOPage, FeedbackDO.class);
    }

    @Override
    public FeedbackDO findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public void insert(FeedbackDO bean) {
        bean.setId(SnowflakeIdUtil.nextId());
        mapper.insert(bean);
    }

    @Override
    public void updateById(FeedbackDO bean) {
        mapper.updateById(bean);
    }

    @Override
    public void delete(FeedbackDO bean) {
        bean.setDelFlag(DelEnum.DELETE.code());
        mapper.updateById(bean);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public FeedbackDO getLastDealByUserId(Long userId) {
        return mapper.selectOne(new LambdaQueryWrapper<FeedbackDO>()
                .select(FeedbackDO::getId, FeedbackDO::getReplyTime)
                .eq(FeedbackDO::getFeedbackUserId, userId)
                .eq(FeedbackDO::getFeedbackStatus, FeedbackStatus.PROCESSED)
                .eq(FeedbackDO::getDelFlag, 0)
                .orderByDesc(FeedbackDO::getReplyTime)
                .last(" limit 1"));
    }

    @Override
    public Long countNoReadFeedback(Long userId) {
        return mapper.selectCount(new LambdaQueryWrapper<FeedbackDO>()
                .eq(FeedbackDO::getFeedbackUserId, userId)
                .eq(FeedbackDO::getFeedbackStatus, FeedbackStatus.PROCESSED)
                .eq(FeedbackDO::getDelFlag, 0)
                .eq(FeedbackDO::getReadFlag, false));
    }

    @Override
    public Long countFeedbackToday(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return mapper.selectCount(new LambdaQueryWrapper<FeedbackDO>()
                .eq(FeedbackDO::getFeedbackUserId, userId)
                .eq(FeedbackDO::getDelFlag, 0)
                .between(FeedbackDO::getFeedbackTime, now.with(LocalTime.MIN), now.with(LocalTime.MAX))
        );
    }

    @Override
    public PageResponse<FeedbackDO> queryFeedbackByPage(PageRequest pageRequest, Long userId) {
        //将所有未读标记为已读
        cancelAllUnreadMsgByUserId(userId);
        LambdaQueryWrapper<FeedbackDO> queryWrapper = Wrappers.lambdaQuery(FeedbackDO.class)
                .eq(FeedbackDO::getFeedbackUserId, userId)
                .eq(FeedbackDO::getFeedbackStatus, FeedbackStatus.PROCESSED)
                .eq(FeedbackDO::getDelFlag, 0)
                .orderByDesc(FeedbackDO::getReplyTime);
        Page<FeedbackDO> FeedbackDOPage = mapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(FeedbackDOPage, FeedbackDO.class);
    }

    @Override
    public void cancelAllUnreadMsgByUserId(Long userId) {
        mapper.update(null, new LambdaUpdateWrapper<FeedbackDO>()
                .eq(FeedbackDO::getFeedbackUserId, userId)
                .eq(FeedbackDO::getReadFlag, false)
                .set(FeedbackDO::getReadFlag, true)
                .set(FeedbackDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public void delRecordsByUserId(Long userId) {
        mapper.update(null, new LambdaUpdateWrapper<FeedbackDO>()
                .eq(FeedbackDO::getFeedbackUserId, userId)
                .eq(FeedbackDO::getDelFlag, 0)
                .set(FeedbackDO::getDelFlag, 1)
                .set(FeedbackDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
    }
}
