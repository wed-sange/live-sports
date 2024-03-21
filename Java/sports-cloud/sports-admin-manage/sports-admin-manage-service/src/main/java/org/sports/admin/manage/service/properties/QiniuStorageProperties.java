package org.sports.admin.manage.service.properties;

import lombok.Data;

/**
 * 七牛云存储配置项
 */
@Data
public class QiniuStorageProperties {
    private String accessKey = "wsZtKHbegPHohfgM4I64_uqwRzk3IgN14diV-2YN";
    private String secretKey ="koDeX0ipT4g04ZaJIIdOijQKHeTds4Fu2vQHmzoz";
    private String bucketName="sports-cloud";
}
