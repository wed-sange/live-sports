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

package org.sports.app.web.controller;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.ActivityPageRequest;
import org.sports.admin.manage.service.service.IActivityService;
import org.sports.admin.manage.service.service.INewsService;
import org.sports.admin.manage.service.vo.ActivityVO;
import org.sports.admin.manage.service.vo.NewsVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.sports.admin.manage.service.constant.CacheConstant.NEWS;


/**
 * @描述: 新闻查询查询相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/20
 * @创建时间: 10:37
 */
@Tag(name =  "app - 新闻管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/news")
public class NewsController {


    @Resource
    private INewsService newsService;

    @PostMapping("/page")
    @Operation(summary = "/新闻分页查询")
    @MLog
    public Result<PageResponse<NewsVO>> getActivityPage(@Validated @RequestBody PageRequest req) {
        return Results.success(newsService.getLiveUserPage(req));
    }
   

    @GetMapping("/getNewsInfo/{id}")
    @Operation(summary = "/查询新闻明细")
    @MLog
    @Cached(cacheType = CacheType.REMOTE, name = NEWS, key = "#id", expire = 1, timeUnit = TimeUnit.DAYS)
    public Result<NewsVO> getMatchCountryList(@PathVariable("id") Long id) {
        return Results.success(newsService.getNewsInfo(id));
    }
}
