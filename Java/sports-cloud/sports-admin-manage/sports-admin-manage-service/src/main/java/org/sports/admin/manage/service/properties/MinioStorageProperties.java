package org.sports.admin.manage.service.properties;

import lombok.Data;

/**
 * Minio存储配置项
 */
@Data
public class MinioStorageProperties {
    private String endPoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
