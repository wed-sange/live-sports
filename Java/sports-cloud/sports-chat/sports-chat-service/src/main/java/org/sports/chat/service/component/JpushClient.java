package org.sports.chat.service.component;


import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @描述: 极光推送-服务端api调用封装
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/07
 * @创建时间: 15:41
 */
@Slf4j
@Component
public class JpushClient {

    @Value("${jpush.appKey}")
    private String appKey = "fc8d210ae169fa35288c7913";
    @Value("${jpush.appName}")
    private String appname = "sports";
    @Value("${jpush.masterSecret}")
    private String masterSecret = "d4439b1348c69f97bf462370";

    @Value("${jpush.apnsProduction}")
    private boolean apnsProduction = false;

    @Value("${jpush.apiAddress}")
    private String apiAddress = "https://push.api.engagelab.cc/v4/push";
    @Resource
    private RestTemplate restTemplate;

    /**
     * 推送成功状态码
     */
    private static final int RESPONSE_OK = 200;

    private static HttpHeaders headers = null;

    /**
     * 极光api请求头信息封装
     *
     * @return 请求头对象
     */
    private HttpHeaders getHttpHeaders() {
        if (headers == null) {
            //封装极光api请求头
            headers = new HttpHeaders();
            headers.add("Authorization", this.getBasicAuthorization());
        }
//        List<MediaType> accept = new ArrayList<>();
//        accept.add(MediaType.APPLICATION_JSON);
//        headers.setAccept(accept);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        return headers;
    }

    /**
     * 获取鉴权字符串
     * 算法：base64(appKey:masterSecret)
     *
     * @return 极光api鉴权字符串
     */
    private String getBasicAuthorization() {
        String encodeKey = appKey + ":" + masterSecret;
        return "Basic " + Base64.getEncoder().encodeToString(encodeKey.getBytes());
    }


    /**
     * -------------------------------------------构建推送对象------------------------------------------------
     */

    /**
     * 推送对象构建
     *
     * @param to       推送目标
     * @param platform 推送平台
     * @param title    推送标题（安卓使用）
     * @param alert    推送内容
     * @param extras   业务扩展内容透传前端
     * @return
     */
    public JPushRequest buildPushRequestBody(Map<String, List<String>> to, List<String> platform, String title, String alert, Map<String, Object> extras) {
        ThirdParty thirdParty = ThirdParty.builder().distribution_new("mtpush_pns").build();
        return JPushRequest.builder()
                //推送平台
                .from(appname)
                //推送目标
                .to(to)
                .requestId(UUID.randomUUID().toString())
                .body(JPushBody.builder()
                        .platform(platform)
                        .options( JPushOptions.builder()
                                .timeToLive(0)
                                .apns_production(apnsProduction)
                                .thirdPartyChannel(ThirdPartyChannel.builder().xiaomi(thirdParty).honor(thirdParty).fcm(thirdParty).huawei(thirdParty).meizu(thirdParty).vivo(thirdParty).oppo(thirdParty).build())
                                .build())
                        .notification(
                                JPushNotification.builder()
                                        .alert(alert)
                                        .android(JPushAndroid.builder()
                                                .alert(alert)
                                                .title(title)
                                                .extras(extras)
                                                .build())
                                        .ios(JPushIos.builder()
                                                .alert(alert)
                                                .extras(extras)
                                                .build())
                                        .build())
                        .build())
                .build();
    }


    /**
     * -------------------------------------------发送推送------------------------------------------------
     */

    /**
     * 极光推送
     *
     * @param jPushRequest
     * @return
     */
    public ResponseEntity<String> sendPush(JPushRequest jPushRequest) {
        log.info("极光推送开始,推送消息体：{}", JSON.toJSONString(jPushRequest));
        ResponseEntity<String> responseEntity = null;
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(jPushRequest), getHttpHeaders());
        try {
            //restTemplate.getMessageConverters().stream().filter(converter -> converter instanceof StringHttpMessageConverter).forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
            //远程调用接口
            log.info("极光推送 HttpEntity = {}",JSON.toJSONString(entity));
            responseEntity = restTemplate.postForEntity(apiAddress,
                    entity,
                    String.class);
            if (Objects.nonNull(responseEntity) && responseEntity.getStatusCodeValue() == RESPONSE_OK) {
                log.info("极光推送成功,推送消息体：{},推送结果：{}", JSON.toJSONString(jPushRequest), JSON.toJSONString(responseEntity));
                return responseEntity;
            }else {
                log.info("极光推送失败,推送消息体：{},推送结果：{}", JSON.toJSONString(jPushRequest), JSON.toJSONString(responseEntity));
            }
        } catch (Exception e) {
            log.error("极光推送失败,推送消息体:{}", JSON.toJSONString(jPushRequest), e);
        }
        return responseEntity;
    }


}
