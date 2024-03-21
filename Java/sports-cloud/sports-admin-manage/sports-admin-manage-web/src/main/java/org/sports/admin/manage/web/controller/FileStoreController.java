/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package org.sports.admin.manage.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.constant.FileConstant;
import org.sports.admin.manage.service.service.FileBucket;
import org.sports.admin.manage.service.service.impl.storage.StorageService;
import org.sports.admin.manage.service.utils.VideoUtils;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.ClientException;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Tag(name =  "后台管理 - 文件上传")
@Validated
@RequiredArgsConstructor
public class FileStoreController {

	@Resource
	private StorageService storageService;

	@PostMapping(value = "/app/source")
	@Operation(summary = "/上传app版本资源文件")
	@MLog
	public Result<String> uploadAppSource(MultipartFile file){
		return Results.success(uploadFileToBucket(FileBucket.APP_SOURCE, file));
	}

	@PostMapping(value = "/user/icon")
	@Operation(summary = "/上传头像")
	@MLog
	public Result<String> uploadIcon(MultipartFile file){
		return Results.success(uploadFileToBucket(FileBucket.USER_ICON, file));
	}

	@PostMapping(value = "/chat/video")
	@Operation(summary = "/上传视频")
	@MLog
	public Result<String> uploadVideo(MultipartFile file) {
		if (file.getSize() > FileConstant.VIDEO_MAX_SIZE) {
			throw new ServiceException("视频大小不能大于" + FileConstant.VIDEO_MAX_SIZE_STR);
		}
		if (!VideoUtils.isVideoFile(file)) {
			throw new ServiceException("视频格式有误，目前只支持"+ FileConstant.VIDEO_TYPE);
		}
		return Results.success(uploadFileToBucket(FileBucket.CHAT_SPACE, file));
	}
	@PostMapping(value = "/common/image")
	@Operation(summary = "/上传普通图片")
	@MLog
	public Result<String> uploadCommonImage(MultipartFile file){
		return Results.success(uploadFileToBucket(FileBucket.COMMON_IMAGE, file));
	}

	private String  uploadFileToBucket(String bucket, MultipartFile file) {

		try {
			String path = storageService.getPath(bucket, file.getOriginalFilename());
			return storageService.upload(file.getBytes(), path);
		} catch (IOException e) {
			throw new ClientException("上传文件失败", BaseErrorCode.CLIENT_ERROR);
		}
	}
}
