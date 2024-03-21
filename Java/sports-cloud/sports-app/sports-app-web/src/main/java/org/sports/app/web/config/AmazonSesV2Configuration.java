package org.sports.app.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
public class AmazonSesV2Configuration {

    @Value("${spring.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.aws.region.static}")
    private String region;

    @Bean
    public SesV2Client sesClient() {
        System.setProperty("aws.accessKeyId", accessKey);
        System.setProperty("aws.secretAccessKey", secretKey);
        return SesV2Client.builder().region(Region.of(region))
                .credentialsProvider(SystemPropertyCredentialsProvider.create()).build();
    }
}
