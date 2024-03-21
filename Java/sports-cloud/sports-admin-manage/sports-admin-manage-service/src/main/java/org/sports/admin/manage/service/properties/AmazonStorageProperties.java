package org.sports.admin.manage.service.properties;

import lombok.Data;

/**
 * 亚马逊云存储配置项
 *
 */
@Data
public class AmazonStorageProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucketName;
}
