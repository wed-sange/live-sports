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

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.dto.CountryDTO;
import org.sports.admin.manage.service.service.ICountryService;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.sports.admin.manage.service.constant.CacheConstant.COUNTRIES;


/**
 * @描述: 国家管理相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/14
 * @创建时间: 13:37
 */
@Tag(name = "app - 国家管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/country")
public class CountryController {


    @Resource
    private ICountryService countryService;

    @PostMapping("/list")
    @Operation(summary = "/国家查询")
    @MLog
    @Cached(cacheType = CacheType.REMOTE, name = COUNTRIES, expire = 1, timeUnit = TimeUnit.DAYS)
    public Result<List<CountryDTO>> getCountryList() {
        List<CountryDTO> list = BeanUtil.convert(countryService.list(), CountryDTO.class);
        // 创建一个自定义的Comparator
        List<CountryDTO> resultList = Lists.newArrayList();
        CountryDTO china = list.stream().filter(item -> item.getCn().equals("中国")).findFirst().orElse(null);
        if (Objects.nonNull(china)) {
            china.setShortName("Z");
            resultList.add(china);
            list.remove(china);
        }
        CountryDTO hk = list.stream().filter(item -> item.getCn().equals("中国香港")).findFirst().orElse(null);
        if (Objects.nonNull(hk)) {
            hk.setShortName("Z");
            resultList.add(hk);
            list.remove(hk);
        }
        CountryDTO mo = list.stream().filter(item -> item.getCn().equals("中国澳门")).findFirst().orElse(null);
        if (Objects.nonNull(mo)) {
            mo.setShortName("Z");
            resultList.add(mo);
            list.remove(mo);
        }
        CountryDTO tw = list.stream().filter(item -> item.getCn().equals("中国台湾")).findFirst().orElse(null);
        if (Objects.nonNull(tw)) {
            tw.setShortName("Z");
            resultList.add(tw);
            list.remove(tw);
        }
        list.forEach(item -> item.setShortName(PinyinUtil.getFirstLetter(item.getCn(), "").substring(0, 1).toUpperCase()));
        list.sort(Comparator.comparing(CountryDTO::getShortName));
        resultList.addAll(list);
        return Results.success(resultList);
    }
}
