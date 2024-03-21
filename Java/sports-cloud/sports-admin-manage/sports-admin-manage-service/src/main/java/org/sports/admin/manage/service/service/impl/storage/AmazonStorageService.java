package org.sports.admin.manage.service.service.impl.storage;

import cn.hutool.core.io.FileUtil;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.service.properties.StorageProperties;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.RemoteException;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 亚马逊云存储
 */
@Slf4j
public class AmazonStorageService extends StorageService {
    private final AmazonS3 s3client;

    public AmazonStorageService(StorageProperties properties) {
        this.properties = properties;
        AWSCredentials credentials = new BasicAWSCredentials(properties.getAmazon().getAccessKey(), properties.getAmazon().getSecretKey());
        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(properties.getAmazon().getRegion()))
                .build();
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            //如果BucketName不存在，则创建
            boolean found = s3client.doesBucketExistV2(properties.getAmazon().getBucketName());
            if (!found) {
                s3client.createBucket(properties.getAmazon().getBucketName());
            }
            // 创建文件上传的元数据
            ObjectMetadata meta = new ObjectMetadata();
            // 设置文件上传长度
            meta.setContentLength(inputStream.available());
            meta.setContentType(getContentType(path));
            meta.setContentDisposition("inline");
            PutObjectRequest request = new PutObjectRequest(properties.getAmazon().getBucketName(), path, inputStream, meta).withCannedAcl(CannedAccessControlList.BucketOwnerFullControl);
            s3client.putObject(request);

        } catch (Exception e) {
            log.error("上传文件失败:",e);
            throw new RemoteException("上传文件失败：", BaseErrorCode.CLIENT_ERROR);
        }

        return properties.getConfig().getDomain() + "/" + path;
    }

    public static void main(String[] args) {
        String countriesIcon = AmazonStorageService.class.getClassLoader().getResource("countries_icon").getPath();
        String contriesJson = AmazonStorageService.class.getClassLoader().getResource("countries_list.json").getPath();

    }
    public static String getContentType(String path){
        String fileExtension = FileUtil.getSuffix(path);
        // 根据文件名的后缀设置ContentType
        String contentType;
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
            case "webp":
            case "bmp":
                contentType =  MediaType.IMAGE_JPEG_VALUE;
                break;
            case "png":
                contentType =  MediaType.IMAGE_PNG_VALUE;
                break;
            case "gif":
                contentType =  MediaType.IMAGE_GIF_VALUE;
                break;
            // 添加其他文件扩展名及对应的ContentType
            default:
                contentType = "application/octet-stream";
        }
        return contentType;
    }
}
