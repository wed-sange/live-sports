package org.sports.admin.manage.service.properties;

import lombok.Data;

/**
 * 腾讯云存储配置项
 *

 */
@Data
public class TencentStorageProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucketName;
}
