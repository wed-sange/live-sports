package org.sports.admin.manage.service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.entity.FeedbackDO;
import org.sports.admin.manage.dao.enums.AppUserNoticeTypeEnum;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.admin.manage.dao.repository.IFeedbackDao;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.converter.FeedbackConvert;
import org.sports.admin.manage.service.service.AppUserNoticeService;
import org.sports.admin.manage.service.service.IFeedbackService;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.vo.FeedbackDetailByAdminVO;
import org.sports.admin.manage.service.vo.FeedbackDetailByUserVO;
import org.sports.admin.manage.service.vo.FeedbackListByAdminVO;
import org.sports.admin.manage.service.vo.FeedbackListByUserVO;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class IFeedbackServiceImpl implements IFeedbackService {
    @Resource
    private IFeedbackDao dao;

    @Resource
    private IPushMessageService pushMessageService;
    @Resource
    private AppUserNoticeService appUserNoticeService;

    @Override
    public void submit(Long userId, String username, FeedbackSubmitRequest request) {
        //每天最多提交5次
        Long today = dao.countFeedbackToday(userId);
        if (today >= 5) {
            throw new ServiceException("今日反馈超过限制");
        }
        //反馈提交
        FeedbackDO feedbackDO = FeedbackConvert.INSTANCE.convertToDo(request);
        //反馈时间
        feedbackDO.setFeedbackTime(LocalDateTime.now(ZoneOffset.UTC));
        //反馈用户
        feedbackDO.setFeedbackUserId(userId);
        feedbackDO.setFeedbackUserName(username);

        dao.insert(feedbackDO);
    }

    @Override
    public void ignore(Long adminId, String adminName, FeedbackIgnoreRequest request) {
        //反馈提交
        FeedbackDO feedbackDO = FeedbackConvert.INSTANCE.convertToDo(request);
        feedbackDO.setFeedbackTime(LocalDateTime.now(ZoneOffset.UTC));
        // 回复用户
        feedbackDO.setReplyUserId(adminId);
        feedbackDO.setReplyUserName(adminName);
        //忽略
        feedbackDO.setIgnoreFlag(true);
        feedbackDO.setFeedbackStatus(FeedbackStatus.PROCESSED);
        feedbackDO.setReplyTime(LocalDateTime.now(ZoneOffset.UTC));
        dao.updateById(feedbackDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reply(Long adminId, String adminName, FeedbackReplyRequest request) {
        //反馈提交
        FeedbackDO feedbackDO = FeedbackConvert.INSTANCE.convertToDo(request);
        //回复时间
        feedbackDO.setFeedbackTime(LocalDateTime.now(ZoneOffset.UTC));
        //反馈用户
        feedbackDO.setReplyUserId(adminId);
        feedbackDO.setReplyUserName(adminName);
        feedbackDO.setReplyTime(LocalDateTime.now(ZoneOffset.UTC));
        feedbackDO.setFeedbackStatus(FeedbackStatus.PROCESSED);
        dao.updateById(feedbackDO);
        //保存信息-系统通知
        FeedbackDO feedbackOld = dao.findById(request.getId());
        AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
        appUserNoticeDO.setType(AppUserNoticeTypeEnum.FEEDBACK.getCode());
        appUserNoticeDO.setUserId(feedbackOld.getFeedbackUserId());
        if(feedbackOld.getFeedbackContent().length() <= 10){
            appUserNoticeDO.setTitle("你提交的" +feedbackOld.getFeedbackContent() + "已处理");
        }else{
            appUserNoticeDO.setTitle("你提交的" +feedbackOld.getFeedbackContent().substring(0,10) + "...已处理");
        }
        appUserNoticeDO.setNotice(feedbackDO.getFeedbackResult());
        appUserNoticeDO.setReadFlag(false);
        appUserNoticeDO.setBizId(feedbackDO.getId().toString());
        appUserNoticeDO.setCreateTime(feedbackDO.getReplyTime());
        appUserNoticeService.save(appUserNoticeDO);
        // 通知前端反馈
        pushMessageService.feedbackReply(feedbackDO.getId(), feedbackOld.getFeedbackUserId(), feedbackDO.getReplyTime(),
                appUserNoticeDO.getId(), appUserNoticeDO.getTitle(), appUserNoticeDO.getNotice());
    }

    @Override
    public PageResponse<FeedbackListByAdminVO> listByAdmin(FeedbackListByAdminPageRequest request) {
        PageResponse<FeedbackDO> FeedbackPage = dao.page(request);
        return FeedbackPage.convert(FeedbackConvert.INSTANCE::convertToListByAdminVO);
    }

    @Override
    public FeedbackDetailByUserVO detailByUser(Long userId, Long id) {
        FeedbackDO byId = dao.findById(id);
        if (!Objects.equals(byId.getFeedbackUserId(), userId)) {
            return null;
        }
        if (Boolean.FALSE.equals(byId.getReadFlag())) {
            //已读
            byId.setReadFlag(true);
            dao.updateById(byId);
        }
        return FeedbackConvert.INSTANCE.convertToDetailByUserVO(byId);
    }

    @Override
    public FeedbackDetailByAdminVO detailByAdmin(Long id) {
        FeedbackDO byId = dao.findById(id);
        return FeedbackConvert.INSTANCE.convertToDetailByAdminVO(byId);
    }

    @Override
    public PageResponse<FeedbackListByUserVO> pageByUser(Long userId, FeedbackListByUserRequest request) {
        return PageUtil.convert(dao.page(userId, request), FeedbackConvert.INSTANCE::convertToListByUserVO);
    }

    @Override
    public FeedbackDO getLastDealByUserId(Long userId) {
        return dao.getLastDealByUserId(userId);
    }

    @Override
    public Long countNoReadFeedback(Long userId) {
        return dao.countNoReadFeedback(userId);
    }

    @Override
    public PageResponse<FeedbackListByUserVO> queryFeedbackByPage(PageRequest pageRequest, Long userId) {
        PageResponse<FeedbackDO> pageResponse = dao.queryFeedbackByPage(pageRequest, userId);
        return pageResponse.convert(each -> BeanUtil.convert(each, FeedbackListByUserVO.class));
    }

    @Override
    public void cancelAllUnreadMsgByUserId(Long userId) {
        dao.cancelAllUnreadMsgByUserId(userId);
    }

    @Override
    public void delRecordsByUserId(Long userId) {
        dao.delRecordsByUserId(userId);
    }
}
