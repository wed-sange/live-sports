package org.sports.springboot.starter.cache.config;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * JetCache配置
 */
@Configuration
@EnableMethodCache(basePackages = "org.sports")
@EnableCreateCacheAnnotation
public class JetCacheConfiguration {

}
