/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.springboot.starter.minio;

import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.springframework.web.multipart.MultipartFile;

/**
 * 抽象一组基本 Minio 操作的接口，由 {@link MinioTemplate} 实现
 *

 */
public interface MinioOperations {
    
    /**
     * 上传文件
     *
     * @param args
     * @return
     */
    ObjectWriteResponse upload(PutObjectArgs args);
    
    /**
     * 上传文件
     *
     * @param multipartFile
     * @return 文件名
     */
    String upload(MultipartFile multipartFile);
}
