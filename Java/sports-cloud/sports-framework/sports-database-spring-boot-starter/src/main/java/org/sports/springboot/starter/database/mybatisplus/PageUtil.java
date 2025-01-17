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

package org.sports.springboot.starter.database.mybatisplus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 */
public class PageUtil {

    /**
     * {@link PageRequest} to {@link Page}
     *
     * @param pageRequest
     * @return
     */
    public static Page convert(PageRequest pageRequest) {
        return convert(pageRequest.getCurrent(), pageRequest.getSize());
    }

    /**
     * {@link PageRequest} to {@link Page}
     *
     * @param current
     * @param size
     * @return
     */
    public static Page convert(long current, long size) {
        return new Page(current, size);
    }

    /**
     * {@link IPage} to {@link PageRequest}
     *
     * @param iPage
     * @return
     */
    public static PageResponse convert(IPage iPage) {
        return buildConventionPage(iPage);
    }

    /**
     * {@link IPage} to {@link PageRequest}
     *
     * @param iPage
     * @param targetClass
     * @param <TARGET>
     * @param <ORIGINAL>
     * @return
     */
    public static <TARGET, ORIGINAL> PageResponse<TARGET> convert(IPage<ORIGINAL> iPage, Class<TARGET> targetClass) {
        iPage.convert(each -> BeanUtil.convert(each, targetClass));
        return buildConventionPage(iPage);
    }

    /**
     * {@link IPage} build to {@link PageRequest}
     *
     * @param iPage
     * @return
     */
    private static PageResponse buildConventionPage(IPage iPage) {
        return PageResponse.builder().current((int) iPage.getCurrent()).size((int) iPage.getSize()).records(iPage.getRecords()).total(iPage.getTotal()).build();
    }


    public static <T, R> PageResponse<R> convert(IPage<T> iPage, Function<? super T, ? extends R> mapper) {
        return new PageResponse<>((int) iPage.getCurrent(), (int) iPage.getSize(), iPage.getTotal(),
                iPage.getRecords().stream().map(mapper).collect(Collectors.toList())
        );
    }
}
