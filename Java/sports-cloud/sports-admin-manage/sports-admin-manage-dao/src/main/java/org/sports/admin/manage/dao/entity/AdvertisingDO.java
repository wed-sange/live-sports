package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.admin.manage.dao.enums.AdvertisingStatus;
import org.sports.admin.manage.dao.enums.AdvertisingType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

/**

 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_admin_advertising")
@Data
public class AdvertisingDO extends MysqlBaseDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 渠道类型
     */
    private AdvertisingType channel;

    /**
     * 状态
     */
    private AdvertisingStatus status;
    /**
     * 文字
     */
    private String text;

    /**
     * 图片展示地址
     */
    private String imgUrl;

    /**
     * 跳转地址
     */
    private String targetUrl;

    /**
     * 备注
     */
    private String remark;
}
