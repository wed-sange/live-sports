package org.sports.admin.manage.service.service;

import java.util.List;

/**
 * 邮件发送接口
 */
public interface IEmailSendService {

    /**
     * 单个邮件发送
     * @param to 接受邮箱
     * @param subject 主题
     * @param content 内容
     * @return
     */
    void sendSimpleText(String to, String subject, String content);

    /**
     * 批量发送邮件
     * @param toList 接受邮箱
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleTextBatch(List<String> toList, String subject, String content);

}
