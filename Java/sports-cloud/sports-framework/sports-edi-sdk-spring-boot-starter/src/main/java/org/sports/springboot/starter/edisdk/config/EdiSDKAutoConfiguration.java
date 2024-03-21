package org.sports.springboot.starter.edisdk.config;

import com.edi.sdk.basketball.BasketballClient;
import com.edi.sdk.football.FootballClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({EdiBasketballProperties.class, EdiFootballProperties.class})
public class EdiSDKAutoConfiguration {

    @Bean
    @ConditionalOnClass(BasketballClient.class)
    @ConditionalOnMissingBean
    public BasketballClient basketballClient(EdiBasketballProperties properties) {
        return new BasketballClient(properties.getUser(), properties.getSecret(), properties.getBaseUrl());
    }

    @Bean
    @ConditionalOnClass(FootballClient.class)
    @ConditionalOnMissingBean
    public FootballClient footballClient(EdiFootballProperties properties) {
        return new FootballClient(properties.getUser(), properties.getSecret(), properties.getBaseUrl());
    }
}
