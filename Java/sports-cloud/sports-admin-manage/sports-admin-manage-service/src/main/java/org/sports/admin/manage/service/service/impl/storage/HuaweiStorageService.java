package org.sports.admin.manage.service.service.impl.storage;


import com.obs.services.ObsClient;
import org.sports.admin.manage.service.properties.StorageProperties;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.RemoteException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 华为云存储
 */
public class HuaweiStorageService extends StorageService {

    public HuaweiStorageService(StorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        ObsClient client = new ObsClient(properties.getHuawei().getAccessKey(),
                properties.getHuawei().getSecretKey(), properties.getHuawei().getEndPoint());
        try {
            client.putObject(properties.getHuawei().getBucketName(), path, inputStream);
            client.close();
        } catch (Exception e) {
            throw new RemoteException("上传文件失败：", BaseErrorCode.CLIENT_ERROR);
        }

        return properties.getConfig().getDomain() + "/" + path;
    }

}
