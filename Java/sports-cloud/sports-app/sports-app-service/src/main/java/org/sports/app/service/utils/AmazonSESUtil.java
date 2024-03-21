//package org.sports.app.service.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.vo.SesEmailVO;
//
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
//import com.amazonaws.services.simpleemail.model.*;
//import org.springframework.util.StringUtils;
//
///**
// * 发邮件工具类
// *
// * @date 2021/12/16 9:29
// */
//@Slf4j
//public class AmazonSESUtil {
//
//    // Replace sender@example.com with your "From" address.
//    // This address must be verified with Amazon SES.
//    static final String FROM = "851683236@qq.com";
//
//    // Replace recipient@example.com with a "To" address. If your account
//    // is still in the sandbox, this address must be verified.
//    static final String TO = "sky.jin@139.com";
//
//    // The configuration set to use for this email. If you do not want to use a
//    // configuration set, comment the following variable and the
//    // .withConfigurationSetName(CONFIGSET); argument below.
//    static final String CONFIGSET = "ConfigSet";
//
//    // The subject line for the email.
//    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
//
//    // The HTML body for the email.
//    static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
//            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
//            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
//            + "AWS SDK for Java</a>";
//
//    // The email body for recipients with non-HTML email clients.
//    static final String TEXTBODY = "This email was sent through Amazon SES "
//            + "using the AWS SDK for Java.";
//
//    public static String sendSes(SesEmailVO vo) {
//        try {
//            AmazonSimpleEmailService client =
//                    AmazonSimpleEmailServiceClientBuilder.standard()
//                            // Replace US_WEST_2 with the AWS Region you're using for
//                            // Amazon SES.
//                            .withRegion(Regions.US_EAST_2).build();
//            SendEmailRequest request = new SendEmailRequest()
//                    .withDestination(
//                            new Destination().withToAddresses(StringUtils.isEmpty(vo.getTo())? TO : vo.getTo()))
//                    .withMessage(new Message()
//                            .withBody(new Body()
//                                    .withHtml(new Content()
//                                            .withCharset("UTF-8").withData(StringUtils.isEmpty(vo.getHtmlBody())? HTMLBODY : vo.getHtmlBody()))
//                                    .withText(new Content()
//                                            .withCharset("UTF-8").withData(StringUtils.isEmpty(vo.getTextBody())? TEXTBODY: vo.getTextBody())))
//                            .withSubject(new Content()
//                                    .withCharset("UTF-8").withData(StringUtils.isEmpty(vo.getSubject())? SUBJECT : vo.getSubject())))
//                    .withSource(StringUtils.isEmpty(vo.getFrom())? FROM : vo.getFrom());
//            // Comment or remove the next line if you are not using a
//            // configuration set
////				.withConfigurationSetName(CONFIGSET);
//            SendEmailResult emailResult = client.sendEmail(request);
//            log.info("Email sent! {}", emailResult);
//            return emailResult.toString();
//        } catch (Exception ex) {
//            log.error("The email was not sent. Error message: ", ex);
//            return ex.getMessage();
//        }
//    }
//
//}
