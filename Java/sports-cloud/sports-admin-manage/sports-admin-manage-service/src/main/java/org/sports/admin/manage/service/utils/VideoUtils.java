package org.sports.admin.manage.service.utils;

import cn.hutool.core.io.file.FileNameUtil;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.service.constant.FileConstant;
import org.springframework.web.multipart.MultipartFile;

public class VideoUtils {

    public static  boolean isVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        if (Strings.isBlank(originalFilename)) {
            return false;
        }
        // 基于Content-Type判断
        if (contentType != null && contentType.startsWith("video/")) {
            return true;
        }
        originalFilename = originalFilename.toLowerCase();
        String extension = FileNameUtil.extName(originalFilename);
        // 基于文件扩展名判断
        if (FileConstant.VIDEO_TYPE.contains(extension)) {
            return true;
        }
        return false;
    }

}
