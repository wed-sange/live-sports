package org.sports.admin.manage.service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.service.service.IEmailSendService;
import org.sports.springboot.starter.base.utils.ValidationUtils;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * eamil发送实现
 */
@Service
@Slf4j
public class EmailSendServiceImpl implements IEmailSendService {

    @Resource
    @Lazy
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username:}")
    private String from;

    @Resource(name = "sendEmailThreadPool")
    @Lazy
    private ThreadPoolExecutor sendEmailThreadPool;

    @Override
    public void sendSimpleText(String to, String subject, String content){
        if(Strings.isEmpty(subject) || Strings.isEmpty(content)){
            throw new ServiceException("发送邮件主题/内容不能为空");
        }
        if(Strings.isEmpty(to) || !ValidationUtils.isEmail(to)){
            throw new ServiceException("发送邮件邮箱有误");
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 邮件发送来源
        simpleMailMessage.setFrom(from);
        // 邮件发送目标
        simpleMailMessage.setTo(to);
        // 设置标题
        simpleMailMessage.setSubject(subject);
        // 设置内容
        simpleMailMessage.setText(content);
        // 发送
        sendEmailThreadPool.execute(()->{
            try {
                javaMailSender.send(simpleMailMessage);
            } catch (Exception e) {
                log.error("发送邮件{}失败:", to, e);
            }
        });
    }

    @Override
    public void sendSimpleTextBatch(List<String> toList, String subject, String content){
        for (String to : toList) {
            sendSimpleText(to, subject, content);
        }
    }

}
