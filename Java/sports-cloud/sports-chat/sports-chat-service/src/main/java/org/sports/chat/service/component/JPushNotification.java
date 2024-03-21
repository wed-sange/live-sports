package org.sports.chat.service.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @描述: JPushNotification
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/7
 * @创建时间: 15:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JPushNotification implements Serializable {
    /**
     * 这个位置的 "alert" 属性（直接在 notification 对象下），是一个快捷定义，
     * 各平台的 alert 信息如果都一样，则可不定义平台下的 alert，以此为准。如果各平台有定义，则覆盖这里的定义
     */
    private String alert;

    private JPushAndroid android;

    private JPushIos ios;
}
