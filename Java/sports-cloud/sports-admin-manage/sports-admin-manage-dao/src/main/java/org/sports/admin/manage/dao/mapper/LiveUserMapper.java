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

package org.sports.admin.manage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sports.admin.manage.dao.entity.LiveUserDO;

import java.util.List;

/**
 * @描述: 直播用户持久层
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 10:44
 */
@Mapper
public interface LiveUserMapper extends BaseMapper<LiveUserDO> {
    @Select("SELECT id FROM t_live_user WHERE identity_type = #{identityType} and del_flag = '0'")
    List<String> getUserIdsByIdentityType(Integer identityType);

    @Select("SELECT id FROM t_live_user WHERE identity_type = 2 and belong_live = #{belongLive}")
    List<String> getAnchorAssi(String belongLive);


}