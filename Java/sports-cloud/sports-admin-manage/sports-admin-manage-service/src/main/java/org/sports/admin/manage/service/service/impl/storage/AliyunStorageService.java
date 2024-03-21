package org.sports.admin.manage.service.service.impl.storage;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.sports.admin.manage.service.properties.StorageProperties;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.RemoteException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云存储
 */
public class AliyunStorageService extends StorageService {
    
    public AliyunStorageService(StorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        OSS client = new OSSClientBuilder().build(properties.getAliyun().getEndPoint(),
                properties.getAliyun().getAccessKeyId(), properties.getAliyun().getAccessKeySecret());
        try {
            client.putObject(properties.getAliyun().getBucketName(), path, inputStream);
        } catch (Exception e) {
            throw new RemoteException("上传文件失败：", BaseErrorCode.CLIENT_ERROR);
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

        return properties.getConfig().getDomain() + "/" + path;
    }

}
