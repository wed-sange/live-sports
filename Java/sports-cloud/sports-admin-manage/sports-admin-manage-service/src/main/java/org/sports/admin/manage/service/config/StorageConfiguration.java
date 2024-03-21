package org.sports.admin.manage.service.config;

import org.sports.admin.manage.service.enums.StorageTypeEnum;
import org.sports.admin.manage.service.properties.StorageProperties;
import org.sports.admin.manage.service.service.impl.storage.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @描述: 存储配置文件
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/9/22
 * @创建时间: 15:37
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@ConditionalOnProperty(prefix = "storage", value = "enabled")
public class StorageConfiguration {

    @Bean
    public StorageService storageService(StorageProperties properties) {
        if (properties.getConfig().getType() == StorageTypeEnum.LOCAL) {
            return new LocalStorageService(properties);
        } else if (properties.getConfig().getType() == StorageTypeEnum.ALIYUN) {
            return new AliyunStorageService(properties);
        } else if (properties.getConfig().getType() == StorageTypeEnum.TENCENT) {
            return new TencentStorageService(properties);
        } else if (properties.getConfig().getType() == StorageTypeEnum.QINIU) {
            return new QiniuStorageService(properties);
        } else if (properties.getConfig().getType() == StorageTypeEnum.HUAWEI) {
            return new HuaweiStorageService(properties);
        } else if (properties.getConfig().getType() == StorageTypeEnum.MINIO) {
            return new MinioStorageService(properties);
        }else if (properties.getConfig().getType() == StorageTypeEnum.AMAZON) {
            return new AmazonStorageService(properties);
        }
        return null;
    }

}
