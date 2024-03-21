package com.edi.sdk.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public abstract class EdiClient {
    private String baseUrl = "https://edi-admin.gte28.com";

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    // APIKey
    private String user;
    private String secret;
    private final OkHttpClient okHttpClient;

    public EdiClient() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .callTimeout(20,TimeUnit.SECONDS)
                .build();
    }

    public EdiClient(String user, String secret) {
        this();
        this.user = user;
        this.secret = secret;
    }

    public EdiClient(String user, String secret, String baseUrl) {
        this();
        this.user = user;
        this.secret = secret;
        this.baseUrl = baseUrl;
    }

    /**
     * 生成带请求参数的地址
     */
    private String generateRequestPath(EdiRequest ediRequest) {
        StringBuilder requestPath = new StringBuilder(ediRequest.getPath() + "?");
        Map<String, Object> paramsMap = JSON.parseObject(JSON.toJSONString(ediRequest),
                new TypeReference<Map<String, Object>>() {
                });
        paramsMap.put("user", user);
        paramsMap.put("secret", secret);
        Set<Entry<String, Object>> paramsSet = paramsMap.entrySet();
        for (Entry<String, Object> param : paramsSet) {
            Object value = param.getValue();
            requestPath.append(param.getKey()).append("=");
            if (value instanceof Collection) {
                requestPath.append(((Collection<?>) value).stream().map(String::valueOf).collect(Collectors.joining(",")));
            } else {
                requestPath.append(param.getValue());
            }
            requestPath.append("&");
        }
        return requestPath.substring(0, requestPath.length() - 1);
    }

    /**
     * 发送请求
     */
    protected String execute(EdiRequest ediRequest) {
        String requestPath = generateRequestPath(ediRequest);
        String finalPath = baseUrl + requestPath;
        log.debug("EDI接口请求路径：{}", finalPath);
        Builder requestBuilder = new Request.Builder();
        switch (ediRequest.getMethod()) {
            case POST: {
                RequestBody requestBody = RequestBody.create(JSON.toJSONString(ediRequest), MEDIA_TYPE);
                requestBuilder.post(requestBody);
                break;
            }
            case GET: {
                requestBuilder.get();
                break;
            }

            default: {
                requestBuilder.get();
            }
        }
        // 设置请求头
        Map<String, String> headerMap = new HashMap<>();
        Headers headers = Headers.of(headerMap);
        requestBuilder.url(finalPath).headers(headers);
        Request request = requestBuilder.build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            ResponseBody body = response.body();
            if (Objects.nonNull(body)) {
                String string = body.string();
                log.debug("EDI接口响应结果：{}", string);
                return string;
            }
        } catch (Exception e) {
            log.error("远程SDK请求失败: ", e);
        }
        return null;
    }
}
