package org.sports.chat.service.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @描述: JPushIos
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/7
 * @创建时间: 16:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JPushIos implements Serializable {

    /**
     * 通知内容
     */
    private String alert;

    /**
     * 扩展参数字段(供业务使用)
     */
    private Map<String,Object> extras;
}
