package org.sports.springboot.starter.base.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 校验工具类
 */
public class ValidationUtils {

    private static final Pattern PATTERN_MOBILE = Pattern.compile("^\\d{7,11}$");

    public static final String PATTERN_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{1,20})$";

    public static final String PATTERN_PASSWD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[0-9A-Za-z]{8,16}$";

    public static boolean isMobile(String mobile) {
        return StringUtils.hasText(mobile) && PATTERN_MOBILE.matcher(mobile).matches();
    }

    public static boolean isEmail(String email) {
        return Pattern.matches(PATTERN_EMAIL, email);
    }

    /**
     * 密码限制：8-16位，包含字母大小组组合+数字
     * @param passwd
     * @return
     */
    public static boolean isPasswd(String passwd){
        return Pattern.matches(PATTERN_PASSWD, passwd);
    }

    public static void main(String[] args) {
        System.out.println(isMobile("123456"));
    }

}
