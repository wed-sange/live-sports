package org.sports.app.service.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RedissonClient;
import org.sports.app.service.service.EmailService;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.satoken.constant.SotokenConstant;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.services.sesv2.model.Message;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EmailServiceImpl
 */
@Log4j2
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.zhSubject}")
    private String zhSubject;

    @Value("${spring.mail.enSubject}")
    private String enSubject;

    @Autowired
    private SesV2Client sesv2Client;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void sendMessageToEmail(String email, Integer lang) {

        String bucketKey = SotokenConstant.TOKEN_KAPTCHA_PRE + UserChannelEnum.APP.getValue() + ":" + email;
        String code = (String) redissonClient.getBucket(bucketKey).get();
        if (StringUtils.isNotBlank(code)) {
            return;
        }
        code = RandomUtil.randomNumbers(6);

        // 引入Template的Context
        Context context = new Context();
        // 设置模板中的变量（分割验证码）
        context.setVariable("verifyCode", Arrays.asList(code.split("")));
        // 第一个参数为模板的名称(html不用写全路径)
        // 这里不用写全路径
        String template = Objects.equals(lang, 2) ?
                "enEmailVerificationCode.html" : "zhEmailVerificationCode.html";
        String process = templateEngine.process(template, context);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            // 邮件的标题
            helper.setSubject(Objects.equals(lang, 2) ? enSubject : zhSubject);
            // 发送者
            helper.setFrom(username);
            // 接收者
            helper.setTo(email);
            // 时间
            helper.setSentDate(new Date());
            // 第二个参数true表示这是一个html文本
            helper.setText(process, true);
            javaMailSender.send(mimeMessage);
            redissonClient.getBucket(bucketKey).set(code, 5, TimeUnit.MINUTES);
        } catch (MessagingException e) {
            throw new ServiceException("邮件发送失败");
        } catch (Exception e) {
            throw new ServiceException("邮件发送失败");
        }

    }


    /**
     * 亚马逊发送邮箱
     */
    public void sendAwsEmail(String verifyCode, String email, Integer lang) {
        // 引入Template的Context
        Context context = new Context();
        // 设置模板中的变量（分割验证码）
        context.setVariable("verifyCode", Arrays.asList(verifyCode.split("")));
        // 第一个参数为模板的名称(html不用写全路径)
        // 这里不用写全路径
        String template = Objects.equals(lang, 2) ?
                "enEmailVerificationCode.html" : "zhEmailVerificationCode.html";
        String process = templateEngine.process(template, context);
        Destination destination = Destination.builder().toAddresses(email).build();
        Content content = Content.builder().data(process).build();
        Content sub = Content.builder().data(Objects.equals(lang, 2) ? enSubject : zhSubject).build();
        Body body = Body.builder().html(content).build();
        Message msg = Message.builder().subject(sub).body(body).build();
        EmailContent emailContent = EmailContent.builder().simple(msg).build();
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination).content(emailContent).fromEmailAddress(username).build();
        try {
            log.info("Attempting to send an email through Amazon SES "
                    + "using the AWS SDK for Java...");
            sesv2Client.sendEmail(emailRequest);
            log.info("email was sent");
        } catch (SesV2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}


