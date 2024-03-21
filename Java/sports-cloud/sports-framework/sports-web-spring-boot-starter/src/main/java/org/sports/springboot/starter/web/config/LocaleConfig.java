package org.sports.springboot.starter.web.config;

import org.apache.logging.log4j.util.Strings;
import org.sports.springboot.starter.web.enums.LanguageEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 配置国际化语言
 */
@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }
    /**
     * 获取请求头国际化信息
     */
    static class I18nLocaleResolver implements LocaleResolver {

        @Override
        public Locale resolveLocale(HttpServletRequest httpServletRequest) {
            try {
                String language = httpServletRequest.getHeader("sports_language");
                if(Strings.isNotEmpty(language)){
                    if(LanguageEnum.ZHCN.getValue().equalsIgnoreCase(language)){
                        return Locale.SIMPLIFIED_CHINESE;
                    }else if(LanguageEnum.ENUS.getValue().equalsIgnoreCase(language)){
                        return Locale.US;
                    }
                }
                return Locale.SIMPLIFIED_CHINESE;
            }catch (Exception e){
                return Locale.SIMPLIFIED_CHINESE;
            }
        }

        @Override
        public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

        }
    }
}