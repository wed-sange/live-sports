package org.sports.springboot.starter.base.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化工具类
 */
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey,String defaultMsg,Object... args) {
        try {
            return messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return defaultMsg;
        }
    }
    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey) {
        return get(msgKey,msgKey);
    }
    /**
     * 获取单个国际化翻译值 并给定默认值
     */
    public static String get(String msgKey,String defaultMsg) {
        return get(msgKey,defaultMsg,null);
    }
}