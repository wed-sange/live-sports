package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.admin.manage.dao.enums.FeedbackType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_admin_feedback")
public class FeedbackDO extends MysqlBaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     * 反馈用户ID
     */
    private Long feedbackUserId;

    /**
     * 反馈用户名
     */
    private String feedbackUserName;

    /**
     * 回复用户ID
     */
    private Long replyUserId;

    /**
     * 回复用户名
     */
    private String replyUserName;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 处理时间
     */
    private LocalDateTime replyTime;

    /**
     * 反馈类型
     */
    private FeedbackType feedbackType;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 反馈结果
     */
    private String feedbackResult;

    /**
     * 是否忽略
     */
    private Boolean ignoreFlag;

    /**
     * 反馈状态 未处理、已处理
     */
    private FeedbackStatus feedbackStatus;

    /**
     * 是否已读
     */
    private Boolean readFlag;

    /**
     * 反馈图片
     */
    private String feedbackImage;
}
