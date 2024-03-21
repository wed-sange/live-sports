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

package org.sports.admin.manage.web;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.sports.admin.manage.dao.entity.CountryDO;
import org.sports.admin.manage.service.dto.CountryDTO;
import org.sports.admin.manage.service.service.FileBucket;
import org.sports.admin.manage.service.service.ICountryService;
import org.sports.admin.manage.service.service.impl.storage.StorageService;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

@Slf4j
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication(scanBasePackages = "org.sports.admin.manage")
@MapperScan("org.sports.admin.manage.dao.mapper")
public class SportsAdminManageApplication implements CommandLineRunner {
    @Resource
    private ICountryService countryService;
    @Resource
    private StorageService storageService;

    @SneakyThrows
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication app = new SpringApplication(SportsAdminManageApplication.class);
        ConfigurableApplicationContext application = app.run(args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \thttp://{}:{}/apis/admin/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));

    }

    @Override
    public void run(String... args) throws Exception {
        if (countryService.count() > 0) {
            return;
        }
        File file = ResourceUtils.getFile("classpath:countries_list.json");
        if (!file.exists()) {
            return;
        }
        String json = FileUtils.readFileToString(file, "UTF-8");
        //初始化国家信息
        List<CountryDTO> countryDTOList = JSON.parseArray(json, CountryDTO.class);
        countryDTOList.forEach(item -> {
            InputStream inputStream = null;
            try {
                File icon = ResourceUtils.getFile("classpath:countries_icon/" + item.getShortName() + ".png");
                if (icon.exists()) {
                    String path = storageService.getPath(FileBucket.COUNTRY_ICON, icon.getName());
                    inputStream = new FileInputStream(icon);
                    String filePath = storageService.upload(inputStream, path);
                    item.setIcon(filePath);
                    CountryDO bean = new CountryDO();
                    countryService.save(BeanUtil.convert(item, bean));
                }
            } catch (Exception e) {
                log.error("上传文件失败:", e);
            } finally {
                if (Objects.nonNull(inputStream)) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("关闭文件异常：", e);
                    }
                }
            }


        });


    }
}
