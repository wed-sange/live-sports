package org.sports.springboot.starter.satoken.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import cn.dev33.satoken.stp.StpUtil;
import org.apache.logging.log4j.util.Strings;
import org.sports.springboot.starter.satoken.constant.SotokenConstant;
import org.sports.springboot.starter.satoken.enums.PlatRoleEnum;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.enums.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SaToken 同一拦截器监听
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Autowired
    private ChatProperties chatProperties;

    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handler ->{
            String[] urlStrs;
            /**
             * 统一总路径【黑白名单、权限】拦截处理
             */
            if(UserChannelEnum.ADMIN.getValue().equals(chatProperties.getChannel()) || UserChannelEnum.LIVE.getValue().equals(chatProperties.getChannel())){
                SaRouterStaff staff = SaRouter.match("/**").notMatch("/initialize/dispatcher-servlet","/doc.html","/webjars/**","/favicon.ico","/v3/**");
                if(Strings.isNotEmpty(chatProperties.getWhiteUrls())){
                    urlStrs = chatProperties.getWhiteUrls().split(",");
                    for (String urlStr : urlStrs) {
                        staff.notMatch(urlStr);
                    }
                }
                /** 登录拦截校验 */
                staff.check(r -> StpUtil.checkLogin());
                /** 角色权限拦截校验 */
                if(UserChannelEnum.ADMIN.getValue().equals(chatProperties.getChannel())){
                    staff.check(r -> StpUtil.checkRole(PlatRoleEnum.ADMIN_ROLE.getName()));
                }else if(UserChannelEnum.LIVE.getValue().equals(chatProperties.getChannel())){
                    staff.check( r -> StpUtil.checkRole(PlatRoleEnum.LIVE_ROLE.getName()));
                }
            }


            /**
             * 特殊指定路径权限校验
             */
            if(Strings.isNotEmpty(chatProperties.getAdminCommonUrls())){
                urlStrs = chatProperties.getAdminCommonUrls().split(",");
                for (String urlStr : urlStrs) {
                    SaRouter.match(urlStr, r -> StpUtil.checkRole(UserRoleEnum.ADMIN_COMMON.getName()));
                }
            }
            if(Strings.isNotEmpty(chatProperties.getAdminSuperUrls())){
                urlStrs = chatProperties.getAdminSuperUrls().split(",");
                for (String urlStr : urlStrs) {
                    SaRouter.match(urlStr, r -> StpUtil.checkRole(UserRoleEnum.ADMIN_SUPER.getName()));
                }
            }
            if(Strings.isNotEmpty(chatProperties.getLiveAnchorUrls())){
                urlStrs = chatProperties.getLiveAnchorUrls().split(",");
                for (String urlStr : urlStrs) {
                    SaRouter.match(urlStr, r -> StpUtil.checkRole(UserRoleEnum.LIVE_ANCHOR.getName()));
                }
            }
            if(Strings.isNotEmpty(chatProperties.getLiveHelperUrls())){
                urlStrs = chatProperties.getLiveHelperUrls().split(",");
                 for (String urlStr : urlStrs) {
                    SaRouter.match(urlStr, r -> StpUtil.checkRole(UserRoleEnum.LIVE_HELPER.getName()));
                }
            }
            if(Strings.isNotEmpty(chatProperties.getLiveOperateUrls())){
                urlStrs = chatProperties.getLiveOperateUrls().split(",");
                for (String urlStr : urlStrs) {
                    SaRouter.match(urlStr, r -> StpUtil.checkRole(UserRoleEnum.LIVE_OPERATE.getName()));
                }

            }
        })).addPathPatterns("/**").order(50);
    }

    // Sa-Token 参数配置，参考文档：https://sa-token.cc
    // 此配置会覆盖 application.yml 中的配置
    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        config.setTokenName(SotokenConstant.TOKEN_NAME);             // token 名称（同时也是 cookie 名称）
        config.setTimeout(-1);    // token 有效期（单位：秒），默认30天，-1代表永不过期
        config.setActiveTimeout(-1);              // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
        config.setIsConcurrent(true);               // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
        config.setIsShare(true);                    // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
        config.setTokenStyle("random-64");               // token 风格 默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik）
        config.setIsLog(true);                     // 是否输出操作日志
        return config;
    }
}
