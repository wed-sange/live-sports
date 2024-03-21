package org.sports.admin.manage.service.service.impl.storage;


import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.service.properties.StorageProperties;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.RemoteException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 */
@Slf4j
public class QiniuStorageService extends StorageService {
    private final UploadManager uploadManager;

    public QiniuStorageService(StorageProperties properties) {
        this.properties = properties;
        uploadManager = new UploadManager(new Configuration(Region.autoRegion()));

    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            String token = Auth.create(properties.getQiniu().getAccessKey(), properties.getQiniu().getSecretKey()).
                    uploadToken(properties.getQiniu().getBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RemoteException("上传文件失败：", BaseErrorCode.CLIENT_ERROR);
            }

            return properties.getConfig().getDomain() + "/" + path;
        } catch (Exception e) {
            log.error("上传失败，原因：", e);
            throw new RemoteException("上传文件失败：", BaseErrorCode.CLIENT_ERROR);
        }
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RemoteException("上传文件失败：", BaseErrorCode.CLIENT_ERROR);
        }
    }

}
