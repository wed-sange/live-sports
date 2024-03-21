/*
 Navicat Premium Data Transfer

 Source Server         : ******
 Source Server Type    : MySQL
 Source Server Version : 50738 (5.7.38)
 Source Host           : ******:3304
 Source Schema         : sports

 Target Server Type    : MySQL
 Target Server Version : 50738 (5.7.38)
 File Encoding         : 65001

 Date: 21/03/2024 15:18:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
                            `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
                            `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
                            `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
                            `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
                            `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
                            `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
                            `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
                            `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
                            `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
                            `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
                            `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
                            `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
                            `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
                            `path` varchar(200) DEFAULT '' COMMENT '路由地址',
                            `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
                            `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
                            `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
                            `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
                            `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
                            `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
                            `status` char(1) DEFAULT '1' COMMENT '菜单状态（1正常 0停用）',
                            `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
                            `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
                            `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT '' COMMENT '备注',
                            PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2058 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
                            `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
                            `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
                            `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
                            `post_sort` int(4) NOT NULL COMMENT '显示顺序',
                            `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
                            `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                            `role_name` varchar(30) NOT NULL COMMENT '角色名称',
                            `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
                            `role_sort` int(4) NOT NULL COMMENT '显示顺序',
                            `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
                            `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
                            `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
                            `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
                            `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
                                 `role_id` bigint(20) NOT NULL COMMENT '角色ID',
                                 `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
                                 PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                                 `role_id` bigint(20) NOT NULL COMMENT '角色ID',
                                 `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
                            `user_name` varchar(30) NOT NULL COMMENT '用户账号',
                            `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
                            `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
                            `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
                            `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
                            `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
                            `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
                            `password` varchar(100) DEFAULT '' COMMENT '密码',
                            `status` char(1) DEFAULT '0' COMMENT '帐号状态（1正常 0停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
                            `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
                            `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
                            `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
                                 `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                 `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
                                 PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                 `role_id` bigint(20) NOT NULL COMMENT '角色ID',
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- Table structure for t_admin_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_activity`;
CREATE TABLE `t_admin_activity` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `title` varchar(256) DEFAULT NULL COMMENT '标题',
                                    `main_pic` varchar(256) DEFAULT NULL COMMENT '主图',
                                    `content` text COMMENT '内容',
                                    `sort_order` tinyint(3) DEFAULT NULL COMMENT '排序值',
                                    `status` tinyint(1) DEFAULT NULL COMMENT '账号状态：0：下架 1：上架',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                    `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                    `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                    `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1764903350566776833 DEFAULT CHARSET=utf8mb4 COMMENT='活动配置';

-- ----------------------------
-- Table structure for t_admin_advertising
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_advertising`;
CREATE TABLE `t_admin_advertising` (
                                       `id` bigint(20) NOT NULL COMMENT 'ID',
                                       `name` varchar(300) DEFAULT NULL COMMENT '名称',
                                       `channel` tinyint(4) NOT NULL COMMENT '渠道类型',
                                       `status` tinyint(4) NOT NULL COMMENT '状态',
                                       `text` varchar(255) DEFAULT NULL COMMENT '滚动广告文字',
                                       `img_url` varchar(255) DEFAULT NULL COMMENT '图片展示地址',
                                       `target_url` varchar(255) DEFAULT NULL COMMENT '跳转地址',
                                       `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                       `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                       `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                       `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告配置表';

-- ----------------------------
-- Table structure for t_admin_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_feedback`;
CREATE TABLE `t_admin_feedback` (
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                    `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                    `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'APP用户是否删除 1删除 0正常',
                                    `id` bigint(20) NOT NULL COMMENT 'ID',
                                    `feedback_user_id` bigint(20) DEFAULT NULL COMMENT '反馈用户ID',
                                    `feedback_user_name` varchar(255) DEFAULT NULL COMMENT '反馈用户名',
                                    `reply_user_id` bigint(20) DEFAULT NULL COMMENT '回复用户ID',
                                    `reply_user_name` varchar(255) DEFAULT NULL COMMENT '回复用户名',
                                    `feedback_time` datetime DEFAULT NULL COMMENT '反馈时间',
                                    `reply_time` datetime DEFAULT NULL COMMENT '处理时间',
                                    `feedback_type` int(11) NOT NULL DEFAULT '1' COMMENT '反馈类型',
                                    `feedback_content` text COMMENT '反馈内容',
                                    `feedback_result` text COMMENT '反馈结果',
                                    `ignore_flag` tinyint(1) DEFAULT NULL COMMENT '忽略标记',
                                    `feedback_status` int(11) NOT NULL DEFAULT '1' COMMENT '反馈状态 1 未处理、2,已处理',
                                    `read_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读',
                                    `feedback_image` text COMMENT '反馈图片',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='意见反馈';

-- ----------------------------
-- Table structure for t_admin_hot_match
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_hot_match`;
CREATE TABLE `t_admin_hot_match` (
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                     `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                     `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                     `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
                                     `id` bigint(20) NOT NULL COMMENT 'ID',
                                     `competition_id` int(11) NOT NULL COMMENT '赛事ID',
                                     `competition_name` varchar(100) DEFAULT NULL COMMENT '名称',
                                     `match_type` int(11) DEFAULT NULL COMMENT '类型 BASKETBALL FOOTBALL',
                                     `full_competition_name` varchar(100) DEFAULT NULL COMMENT '赛事全称',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热门比赛';

-- ----------------------------
-- Table structure for t_admin_sms
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_sms`;
CREATE TABLE `t_admin_sms` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `supplier` tinyint(1) DEFAULT NULL COMMENT '平台类型（1阿里云，2腾讯云）\n',
                               `sms_no` int(11) DEFAULT NULL COMMENT '标号\n',
                               `access_key_id` varchar(64) DEFAULT NULL COMMENT 'accessKey',
                               `access_key_secret` varchar(64) DEFAULT NULL COMMENT 'accessKeySecret',
                               `signature` varchar(50) DEFAULT NULL COMMENT '短信签名',
                               `template_id` varchar(128) DEFAULT NULL COMMENT '短信模板ID',
                               `sdk_app_id` varchar(128) DEFAULT NULL COMMENT 'appId/senderId(阿里云/腾讯云专属)',
                               `open_status` int(1) DEFAULT NULL COMMENT '启用状态 false关闭 true开启',
                               `remark` varchar(128) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770418957007208449 DEFAULT CHARSET=utf8mb4 COMMENT='短信配置';

-- ----------------------------
-- Table structure for t_admin_sms_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_sms_rule`;
CREATE TABLE `t_admin_sms_rule` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `va_type` text COMMENT '验证方式json数组[111,1111]\n',
                                    `use_type` int(2) DEFAULT NULL COMMENT '使用方式： 1随机使用 2轮替使用',
                                    `ratate_num` int(11) DEFAULT NULL COMMENT '轮替次数',
                                    `error_num` int(11) DEFAULT NULL COMMENT '异常次数',
                                    `contact_info` varchar(50) DEFAULT NULL COMMENT '系统通知联系人',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770372951196332033 DEFAULT CHARSET=utf8mb4 COMMENT='短信功能配置';

-- ----------------------------
-- Table structure for t_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_user`;
CREATE TABLE `t_admin_user` (
                                `id` bigint(20) NOT NULL,
                                `account` varchar(30) DEFAULT NULL COMMENT '账号',
                                `name` varchar(50) DEFAULT NULL COMMENT '昵称',
                                `passwd` varchar(50) NOT NULL COMMENT '密码',
                                `yn_valid` int(2) DEFAULT '1' COMMENT '是否有效 1是 0否',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='后台管理人员信息';

-- ----------------------------
-- Table structure for t_admin_version
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_version`;
CREATE TABLE `t_admin_version` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `channel` tinyint(1) DEFAULT NULL COMMENT '渠道：1：安卓 2：IOS',
                                   `version` varchar(50) DEFAULT NULL COMMENT '版本号',
                                   `remarks` varchar(250) DEFAULT NULL COMMENT '版本说明',
                                   `forced_update` tinyint(1) DEFAULT NULL COMMENT '是否强制更新：0：否 1：是',
                                   `source_url` varchar(255) DEFAULT NULL COMMENT '版本资源',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                   `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                   `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                   `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1733068185107062785 DEFAULT CHARSET=utf8mb4 COMMENT='app版本配置';

-- ----------------------------
-- Table structure for t_app_anchor_mute_user
-- ----------------------------
DROP TABLE IF EXISTS `t_app_anchor_mute_user`;
CREATE TABLE `t_app_anchor_mute_user` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                          `anchor_id` bigint(20) NOT NULL COMMENT '主播ID',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                          `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                          `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                          `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                          `pic` varchar(100) DEFAULT NULL COMMENT '图片',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770709711684685825 DEFAULT CHARSET=utf8mb4 COMMENT='主播禁言列表';

-- ----------------------------
-- Table structure for t_app_follow
-- ----------------------------
DROP TABLE IF EXISTS `t_app_follow`;
CREATE TABLE `t_app_follow` (
                                `id` bigint(20) NOT NULL,
                                `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
                                `follow_type` int(2) NOT NULL COMMENT '关注类型：(1主播 2比赛)',
                                `match_type` int(2) DEFAULT NULL COMMENT '比赛类型',
                                `biz_id` bigint(20) DEFAULT NULL COMMENT '主播ID或者比赛ID',
                                `is_friend` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否好友（1：是 0 ：否）',
                                `is_focus` tinyint(1) DEFAULT '1' COMMENT '是否关注',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                `del_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                                `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='我的关注和订阅';

-- ----------------------------
-- Table structure for t_app_news
-- ----------------------------
DROP TABLE IF EXISTS `t_app_news`;
CREATE TABLE `t_app_news` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              `title` varchar(256) DEFAULT NULL COMMENT '标题',
                              `tournament` varchar(256) DEFAULT NULL COMMENT '联赛',
                              `content` text COMMENT '内容',
                              `source_url` varchar(256) DEFAULT NULL COMMENT '来源地址',
                              `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                              `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                              `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                              `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                              `pic` varchar(100) DEFAULT NULL COMMENT '图片',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `id` (`id`),
                              KEY `t_app_news_source_url_IDX` (`source_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1770706267179593729 DEFAULT CHARSET=utf8mb4 COMMENT='球类新闻';

-- ----------------------------
-- Table structure for t_app_notice_cofing
-- ----------------------------
DROP TABLE IF EXISTS `t_app_notice_cofing`;
CREATE TABLE `t_app_notice_cofing` (
                                       `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                       `yn_follow_match` int(2) DEFAULT '1' COMMENT '是否开启关注比赛通知 1是 0否',
                                       `yn_live_open` int(2) DEFAULT '1' COMMENT '是否开启主播开播通知 1是 0否',
                                       `update_time` datetime NOT NULL COMMENT '修改时间',
                                       PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='APP用户通知设置';

-- ----------------------------
-- Table structure for t_app_pinned_user
-- ----------------------------
DROP TABLE IF EXISTS `t_app_pinned_user`;
CREATE TABLE `t_app_pinned_user` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
                                     `anchor_id` bigint(20) NOT NULL COMMENT '主播ID',
                                     `type` tinyint(1) DEFAULT NULL COMMENT '类型：1：主播置顶； 2：用户置顶',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                     `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                     `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                     `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                     `operate_id` bigint(20) NOT NULL COMMENT '助理或者操作人员ID',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770710567314313217 DEFAULT CHARSET=utf8mb4 COMMENT='主播用户置顶';

-- ----------------------------
-- Table structure for t_app_user
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user`;
CREATE TABLE `t_app_user` (
                              `id` bigint(20) NOT NULL,
                              `tel` varchar(20) DEFAULT NULL COMMENT '手机号',
                              `area_code` varchar(20) DEFAULT NULL COMMENT '手机号区号',
                              `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
                              `name` varchar(50) DEFAULT NULL COMMENT '昵称',
                              `head` varchar(200) DEFAULT NULL COMMENT '头像',
                              `passwd` varchar(64) DEFAULT NULL COMMENT '密码',
                              `lv_num` int(2) NOT NULL COMMENT '等级',
                              `lv_name` varchar(20) NOT NULL COMMENT '等级名称',
                              `growth_value` int(10) DEFAULT '0' COMMENT '成长值',
                              `growth_value_next` int(10) DEFAULT '0' COMMENT '下一阶段成长值',
                              `register_addr` varchar(50) DEFAULT NULL COMMENT '注册地址',
                              `name_last_time` datetime DEFAULT NULL COMMENT '昵称上次修改时间',
                              `yn_cancel` int(2) DEFAULT '0' COMMENT '是否注销 1是 0否',
                              `yn_forbidden` int(2) DEFAULT '0' COMMENT '是否禁言 0否 1普通禁言 2永久禁言',
                              `forbidden_day` int(10) DEFAULT NULL COMMENT '禁言天数',
                              `forbidden_time` datetime DEFAULT NULL COMMENT '禁言期限',
                              `forbidden_descp` varchar(200) DEFAULT NULL COMMENT '禁言原因',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='APP用户信息';

-- ----------------------------
-- Table structure for t_app_user_growth
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user_growth`;
CREATE TABLE `t_app_user_growth` (
                                     `id` bigint(20) NOT NULL,
                                     `type` int(2) NOT NULL COMMENT '获取成长值类型(1直播聊天互动 2观看直播)',
                                     `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                     `event_number` int(10) NOT NULL COMMENT '触发次数',
                                     `event_growth` int(10) NOT NULL COMMENT '获得成长值',
                                     `begin_time` datetime DEFAULT NULL COMMENT '统计开始时间',
                                     `end_time` datetime DEFAULT NULL COMMENT '统计截止时间',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='APP用户成长值信息';

-- ----------------------------
-- Table structure for t_app_user_live_history
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user_live_history`;
CREATE TABLE `t_app_user_live_history` (
                                           `id` bigint(20) NOT NULL,
                                           `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
                                           `live_user_id` bigint(20) DEFAULT NULL COMMENT '主播ID',
                                           `in_time` datetime DEFAULT NULL COMMENT '进入直播间时间',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                           PRIMARY KEY (`id`),
                                           KEY `t_app_user_live_history_user_id_IDX` (`user_id`,`live_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='APP用户浏览记录';

-- ----------------------------
-- Table structure for t_app_user_live_share
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user_live_share`;
CREATE TABLE `t_app_user_live_share` (
                                         `id` bigint(20) NOT NULL,
                                         `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
                                         `live_id` bigint(20) DEFAULT NULL COMMENT '直播间ID',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='app用户直播分享记录';

-- ----------------------------
-- Table structure for t_app_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user_notice`;
CREATE TABLE `t_app_user_notice` (
                                     `id` bigint(20) NOT NULL COMMENT '主ID',
                                     `type` int(2) NOT NULL COMMENT '通知类型(1反馈结果 2禁言通知 3解禁通知)',
                                     `user_id` bigint(20) NOT NULL COMMENT '通知用户ID',
                                     `title` varchar(100) NOT NULL COMMENT '通知标题',
                                     `notice` varchar(500) DEFAULT NULL COMMENT '通知内容',
                                     `read_flag` int(2) NOT NULL COMMENT '是否已读(1是 0否)',
                                     `biz_id` varchar(255) DEFAULT NULL COMMENT '业务ID',
                                     `create_time` datetime NOT NULL COMMENT '通知时间',
                                     `del_flag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='APP用户通知';

-- ----------------------------
-- Table structure for t_app_user_regid
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user_regid`;
CREATE TABLE `t_app_user_regid` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `user_id` varchar(256) DEFAULT NULL COMMENT '用户ID',
                                    `reg_id` varchar(256) DEFAULT NULL COMMENT '极光推送ID',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                    `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                    `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                    `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770628984246665217 DEFAULT CHARSET=utf8mb4 COMMENT='用户ID和极光推送注册ID绑定关系';

-- ----------------------------
-- Table structure for t_chat_group
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_group`;
CREATE TABLE `t_chat_group` (
                                `id` bigint(20) NOT NULL,
                                `group_id` varchar(30) NOT NULL COMMENT '群组ID',
                                `name` varchar(100) DEFAULT NULL COMMENT '群名称',
                                `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                `del_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                                `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天组信息';

-- ----------------------------
-- Table structure for t_chat_group_user
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_group_user`;
CREATE TABLE `t_chat_group_user` (
                                     `id` bigint(20) NOT NULL,
                                     `group_id` varchar(30) DEFAULT NULL COMMENT '群组ID',
                                     `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
                                     `nick` varchar(100) DEFAULT NULL COMMENT '用户昵称',
                                     `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
                                     `status` tinyint(4) DEFAULT NULL COMMENT '是否在线 0在线 1不在线',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                     `del_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                                     `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                     `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                     `leave_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否离开1：离开0未离开',
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `t_chat_group_user_UN` (`group_id`,`user_id`),
                                     KEY `t_chat_group_user_group_id_IDX` (`group_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='群组用户信息';

-- ----------------------------
-- Table structure for t_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message` (
                                  `id` bigint(20) NOT NULL,
                                  `from_id` varchar(30) DEFAULT NULL COMMENT '发送用户ID',
                                  `to_id` varchar(30) DEFAULT NULL COMMENT '目标用户ID',
                                  `anchor_id` varchar(30) DEFAULT NULL COMMENT '这条消息的主播ID（私聊独有）',
                                  `cmd` tinyint(1) DEFAULT NULL COMMENT '消息命令码',
                                  `msg_type` tinyint(1) DEFAULT NULL COMMENT ' 消息类型;(如：0:text、1:image、2:voice、3:video、4:music、5:news)',
                                  `chat_type` tinyint(1) DEFAULT NULL COMMENT '聊天类型;(如1:公聊、2私聊)',
                                  `avatar` varchar(200) DEFAULT NULL COMMENT '发送者头像',
                                  `nick` varchar(200) DEFAULT NULL COMMENT '发送者昵称',
                                  `identity_type` tinyint(1) DEFAULT NULL COMMENT '发送者身份身份(0：普通用户，1主播 2助手 3运营)',
                                  `level` tinyint(1) DEFAULT NULL COMMENT '级别',
                                  `content` varchar(500) DEFAULT NULL COMMENT '消息内容',
                                  `group_id` varchar(30) DEFAULT NULL COMMENT '群组ID',
                                  `create_time` timestamp(3) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` timestamp(3) NULL DEFAULT NULL COMMENT '修改时间',
                                  `del_flag` tinyint(1) DEFAULT NULL COMMENT 'APP用户是否删除 1删除 0正常',
                                  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                  `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                  `sent` tinyint(1) DEFAULT NULL COMMENT '是否已发送: 0 未发送 1 已发送',
                                  `readable` tinyint(1) DEFAULT '0' COMMENT '是否已读：0 未读 1 已读',
                                  `user_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否用户删除',
                                  `anchor_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主播删除',
                                  PRIMARY KEY (`id`),
                                  KEY `create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='群组用户信息';

-- ----------------------------
-- Table structure for t_chat_user
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_user`;
CREATE TABLE `t_chat_user` (
                               `id` bigint(20) NOT NULL,
                               `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
                               `nick` varchar(100) DEFAULT NULL COMMENT '昵称',
                               `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
                               `lv_name` varchar(20) DEFAULT NULL COMMENT '等级名称',
                               `lv_num` int(2) DEFAULT NULL COMMENT '等级',
                               `identity` tinyint(1) NOT NULL COMMENT '用户身份：0-普通用户   1-主播   2-主播助理 3-运营',
                               `sign` varchar(300) DEFAULT NULL COMMENT '签名',
                               `terminal` varchar(300) DEFAULT NULL COMMENT '中断',
                               `status` tinyint(1) DEFAULT NULL COMMENT '在线状态',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                               `del_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                               `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                               `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                               PRIMARY KEY (`id`),
                               KEY `t_chat_user_del_flag_IDX` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天人员信息';

-- ----------------------------
-- Table structure for t_dic_country
-- ----------------------------
DROP TABLE IF EXISTS `t_dic_country`;
CREATE TABLE `t_dic_country` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                 `cn` varchar(50) DEFAULT NULL COMMENT '中文',
                                 `en` varchar(50) DEFAULT NULL COMMENT '英文',
                                 `full` varchar(100) DEFAULT NULL COMMENT '国家全称',
                                 `short_name` varchar(20) DEFAULT NULL COMMENT '国家代号',
                                 `icon` varchar(100) DEFAULT NULL COMMENT '国家图标',
                                 `dialing_code` varchar(50) DEFAULT NULL COMMENT '电话区号',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                 `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                                 `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                                 `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1735150450343936001 DEFAULT CHARSET=utf8mb4 COMMENT='国家字典表';

-- ----------------------------
-- Table structure for t_live
-- ----------------------------
DROP TABLE IF EXISTS `t_live`;
CREATE TABLE `t_live` (
                          `id` bigint(20) NOT NULL COMMENT 'ID',
                          `user_id` bigint(20) NOT NULL COMMENT '主播ID',
                          `nick_name` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
                          `user_logo` varchar(255) NOT NULL DEFAULT '' COMMENT '主播头像',
                          `title_page` varchar(255) NOT NULL DEFAULT '' COMMENT '封面',
                          `notice` text NOT NULL COMMENT '公告',
                          `first_message` text NOT NULL COMMENT '首条消息',
                          `hot_value` bigint(20) NOT NULL DEFAULT '0' COMMENT '热度值',
                          `hot_init_value` bigint(20) DEFAULT NULL COMMENT '初始热度值',
                          `hot_time_no` int(11) DEFAULT NULL COMMENT '间隔30分钟对应次数',
                          `hot_time_value` bigint(20) DEFAULT NULL COMMENT '间隔30分钟已获取热度值',
                          `competition_id` varchar(255) DEFAULT NULL COMMENT '赛事ID',
                          `competition_name` varchar(255) DEFAULT NULL COMMENT '赛事名称',
                          `match_id` int(11) DEFAULT NULL COMMENT '比赛ID',
                          `match_type` int(11) DEFAULT NULL COMMENT '类型 2.BASKETBALL 1.FOOTBALL',
                          `match_time` int(11) DEFAULT NULL COMMENT '比赛时间',
                          `home_team_name` varchar(255) DEFAULT NULL COMMENT '主队',
                          `home_team_logo` varchar(255) DEFAULT NULL COMMENT '主队LOGO',
                          `away_team_name` varchar(255) DEFAULT NULL COMMENT '客队',
                          `away_team_logo` varchar(255) DEFAULT NULL COMMENT '客队LOGO',
                          `source_url` varchar(255) DEFAULT NULL COMMENT '来源地址',
                          `play_url` varchar(255) DEFAULT NULL COMMENT '播放地址',
                          `open_time` datetime DEFAULT NULL COMMENT '开播时间',
                          `close_time` datetime DEFAULT NULL COMMENT '关播时间',
                          `live_status` int(11) DEFAULT NULL COMMENT '直播状态 2直播中 3已关播',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                          `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                          `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                          `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='直播';

-- ----------------------------
-- Table structure for t_live_heat_config
-- ----------------------------
DROP TABLE IF EXISTS `t_live_heat_config`;
CREATE TABLE `t_live_heat_config` (
                                      `id` bigint(20) NOT NULL,
                                      `base_heat` int(10) DEFAULT '100' COMMENT '基础热度值（非热门）',
                                      `base_heat_hot` int(10) DEFAULT '300' COMMENT '基础热度值（热门）',
                                      `people_number_ratio` int(10) DEFAULT '50' COMMENT '人数系数值',
                                      `msg_send_ratio` int(11) DEFAULT '1' COMMENT '直播间消息发送系数',
                                      `share_ratio` int(11) DEFAULT '1' COMMENT '直播间分享系数',
                                      `streaming_address` varchar(300) DEFAULT NULL COMMENT '推流地址',
                                      `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
                                      `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='直播间热度管理';

-- ----------------------------
-- Table structure for t_live_openinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_live_openinfo`;
CREATE TABLE `t_live_openinfo` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                   `anchor_id` bigint(20) NOT NULL COMMENT '主播ID',
                                   `title_page` varchar(256) DEFAULT NULL COMMENT '直播封面',
                                   `notice` varchar(1024) DEFAULT NULL COMMENT '直播公告',
                                   `first_message` varchar(1024) DEFAULT NULL COMMENT '开播首条聊天',
                                   `remark` varchar(30) DEFAULT NULL COMMENT '备注名称',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                   `used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否使用1使用中0未使用',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770704153736933377 DEFAULT CHARSET=utf8mb4 COMMENT='直播开播信息';

-- ----------------------------
-- Table structure for t_live_user
-- ----------------------------
DROP TABLE IF EXISTS `t_live_user`;
CREATE TABLE `t_live_user` (
                               `id` bigint(20) NOT NULL,
                               `account` varchar(30) DEFAULT NULL COMMENT '账号',
                               `identity_type` int(2) NOT NULL COMMENT '身份(1主播 2助手 3运营)',
                               `name` varchar(50) DEFAULT NULL COMMENT '名字',
                               `nick_name` varchar(100) DEFAULT NULL COMMENT '昵称',
                               `head` varchar(200) DEFAULT NULL COMMENT '头像',
                               `notice` varchar(300) DEFAULT NULL COMMENT '主播公告',
                               `remarks` varchar(300) DEFAULT NULL COMMENT '备注',
                               `passwd` varchar(50) NOT NULL COMMENT '密码',
                               `belong_live` bigint(20) DEFAULT NULL COMMENT '所属主播(助手特有)',
                               `possess_live` longtext COMMENT '拥有主播（运营特有）',
                               `yn_forbidden` int(2) DEFAULT '0' COMMENT '是否封禁 0否 1普通封禁 2永久封禁',
                               `yn_cancel` int(2) DEFAULT '0' COMMENT '是否注销 1是 0否',
                               `forbidden_day` int(10) DEFAULT NULL COMMENT '封禁天数',
                               `forbidden_descp` varchar(255) DEFAULT NULL COMMENT '封禁原因',
                               `forbidden_time` datetime DEFAULT NULL COMMENT '封禁期限',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                               `del_flag` tinyint(1) DEFAULT NULL COMMENT '是否删除',
                               `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
                               `updater` varchar(100) DEFAULT NULL COMMENT '更新人',
                               `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
                               `set_open_info` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否配置过直播信息：0未配置1配置过',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `account` (`account`),
                               KEY `t_live_user_del_flag_IDX` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='直播人员信息';

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  103, 'spadmin', '超级管理员', '00', 'spadmin@163.com', '15888888888', '1', '', '1b2753dfcb111675497b08509b09e5d2', '1', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '超级管理员');
insert into sys_user values(2,  105, 'admin',    '管理员', '00', 'admin@qq.com',  '15666666666', '1', '', '1b2753dfcb111675497b08509b09e5d2', '1', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '管理员');


-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '科技',   0, '科技', '15888888888', 'lkr@qq.com', '1', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(101,  100, '0,100',      '技术部门', 1, '技术', '15888888888', 'js@qq.com', '1', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(102,  100, '0,100',      '运营部门', 2, '运营', '15888888888', 'yy@qq.com', '1', '0', 'admin', sysdate(), '', null);


-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, '1', 'admin', sysdate(), '', null, '');
insert into sys_post values(2, 'se',   '项目经理',  2, '1', 'admin', sysdate(), '', null, '');
insert into sys_post values(3, 'hr',   '人力资源',  3, '1', 'admin', sysdate(), '', null, '');
insert into sys_post values(4, 'user', '普通员工',  4, '1', 'admin', sysdate(), '', null, '');



-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '超级管理员',  'spadmin',  1, 1, 1, 1, '1', '0', 'admin', sysdate(), '', null, '超级管理员');
insert into sys_role values('2', '普通角色',    'common', 2, 2, 1, 1, '1', '0', 'admin', sysdate(), '', null, '普通角色');


INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('系统管理',0,10,'system',NULL,'',1,0,'M','0','1','','system','admin','2024-01-03 16:08:42','test昵称','2024-01-03 16:54:41','系统管理目录'),
                                                                                                                                                                                                ('用户管理',1,1,'role/list','system/user/index','',1,0,'C','0','1','system:user:list','user','admin','2024-01-03 16:08:42','test昵称','2024-01-03 16:58:48','用户管理菜单'),
                                                                                                                                                                                                ('角色管理',1,2,'role','system/role/index','',1,0,'C','0','1','system:role:list','peoples','admin','2024-01-03 16:08:42','',NULL,'角色管理菜单'),
                                                                                                                                                                                                ('菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','1','system:menu:list','tree-table','admin','2024-01-03 16:08:42','',NULL,'菜单管理菜单'),
                                                                                                                                                                                                ('部门管理',1,4,'dept','system/dept/index','',1,0,'C','0','1','system:dept:list','tree','admin','2024-01-03 16:08:42','',NULL,'部门管理菜单'),
                                                                                                                                                                                                ('岗位管理',1,5,'post','system/post/index','',1,0,'C','0','1','system:post:list','post','admin','2024-01-03 16:08:42','',NULL,'岗位管理菜单'),
                                                                                                                                                                                                ('用户查询',100,1,'','','',1,0,'F','0','1','system:user:query','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('用户新增',100,2,'','','',1,0,'F','0','1','system:user:add','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('用户修改',100,3,'','','',1,0,'F','0','1','system:user:edit','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('用户删除',100,4,'','','',1,0,'F','0','1','system:user:remove','#','admin','2024-01-03 16:08:42','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('用户导出',100,5,'','','',1,0,'F','0','1','system:user:export','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('用户导入',100,6,'','','',1,0,'F','0','1','system:user:import','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('重置密码',100,7,'','','',1,0,'F','0','1','system:user:resetPwd','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('角色查询',101,1,'','','',1,0,'F','0','1','system:role:query','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('角色新增',101,2,'','','',1,0,'F','0','1','system:role:add','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('角色修改',101,3,'','','',1,0,'F','0','1','system:role:edit','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('角色删除',101,4,'','','',1,0,'F','0','1','system:role:remove','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('角色导出',101,5,'','','',1,0,'F','0','1','system:role:export','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('菜单查询',102,1,'','','',1,0,'F','0','1','system:menu:query','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('菜单新增',102,2,'','','',1,0,'F','0','1','system:menu:add','#','admin','2024-01-03 16:08:42','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('菜单修改',102,3,'','','',1,0,'F','0','1','system:menu:edit','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('菜单删除',102,4,'','','',1,0,'F','0','1','system:menu:remove','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('部门查询',103,1,'','','',1,0,'F','0','1','system:dept:query','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('部门新增',103,2,'','','',1,0,'F','0','1','system:dept:add','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('部门修改',103,3,'','','',1,0,'F','0','1','system:dept:edit','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('部门删除',103,4,'','','',1,0,'F','0','1','system:dept:remove','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('岗位查询',104,1,'','','',1,0,'F','0','1','system:post:query','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('岗位新增',104,2,'','','',1,0,'F','0','1','system:post:add','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('岗位修改',104,3,'','','',1,0,'F','0','1','system:post:edit','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('岗位删除',104,4,'','','',1,0,'F','0','1','system:post:remove','#','admin','2024-01-03 16:08:42','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('岗位导出',104,5,'','','',1,0,'F','0','1','system:post:export','#','admin','2024-01-03 16:08:42','',NULL,''),
                                                                                                                                                                                                ('主播管理',0,3,'anchor','',NULL,1,0,'M','0','1','','date','test昵称','2024-01-03 16:35:19','test昵称','2024-01-03 17:07:17',''),
                                                                                                                                                                                                ('主播列表',2000,4,'list','anchor/manage/index',NULL,1,0,'C','0','1','','#','test昵称','2024-01-03 16:45:42','test昵称','2024-01-04 11:09:50',''),
                                                                                                                                                                                                ('用户管理',0,1,'manager','',NULL,1,0,'M','0','1','','education','test昵称','2024-01-03 16:47:54','test昵称','2024-01-03 17:03:58',''),
                                                                                                                                                                                                ('用户列表',2002,1,'list','manager/list/index',NULL,1,0,'C','0','1','','#','test昵称','2024-01-03 16:48:54','test昵称','2024-01-04 11:08:29',''),
                                                                                                                                                                                                ('账号信息',2000,5,'account','anchor/account/index',NULL,1,0,'C','0','1',NULL,'#','test昵称','2024-01-03 17:12:40','',NULL,''),
                                                                                                                                                                                                ('热门比赛',0,5,'hot','hot/list',NULL,1,0,'C','0','1','','cascader','test昵称','2024-01-03 17:13:23','test昵称','2024-01-03 17:14:16',''),
                                                                                                                                                                                                ('运营管理',0,5,'operations',NULL,NULL,1,0,'M','0','1',NULL,'edit','test昵称','2024-01-03 17:15:38','',NULL,''),
                                                                                                                                                                                                ('广告配置',2009,1,'advertisment','operations/advertisment/index',NULL,1,0,'C','0','1','','#','test昵称','2024-01-03 17:16:29','test昵称','2024-01-03 17:16:39',''),
                                                                                                                                                                                                ('意见反馈',2009,2,'feedback','operations/feedback/index',NULL,1,0,'C','0','1',NULL,'#','test昵称','2024-01-03 17:17:24','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('活动配置',2009,3,'activity','operations/activity/index',NULL,1,0,'C','0','1','','#','test昵称','2024-01-03 17:18:29','test昵称','2024-01-03 17:19:05',''),
                                                                                                                                                                                                ('聊天记录',0,7,'chats','chats/list',NULL,1,0,'C','0','1','chat:message:record','log','test昵称','2024-01-03 17:21:47','test昵称','2024-01-04 11:50:43',''),
                                                                                                                                                                                                ('公共配置',0,8,'common',NULL,NULL,1,0,'M','0','1',NULL,'monitor','test昵称','2024-01-03 17:23:25','',NULL,''),
                                                                                                                                                                                                ('参数配置',2015,1,'setting','common/setting',NULL,1,0,'C','0','1',NULL,'#','test昵称','2024-01-03 17:24:07','',NULL,''),
                                                                                                                                                                                                ('版本更新',2015,2,'version','common/version',NULL,1,0,'C','0','1',NULL,'#','test昵称','2024-01-03 17:24:49','',NULL,''),
                                                                                                                                                                                                ('App用户查询',2003,1,'',NULL,NULL,1,0,'F','0','1','app:user:query','#','test昵称','2024-01-04 10:01:09','',NULL,''),
                                                                                                                                                                                                ('App用户禁言/解禁',2003,2,'',NULL,NULL,1,0,'F','0','1','app:user:forbidden','#','test昵称','2024-01-04 10:03:48','',NULL,''),
                                                                                                                                                                                                ('主播列表查询',2001,1,'',NULL,NULL,1,0,'F','0','1','anchor:manage:query','#','test昵称','2024-01-04 11:09:16','',NULL,''),
                                                                                                                                                                                                ('关播操作',2001,3,'',NULL,NULL,1,0,'F','0','1','anchor:manage:close','#','test昵称','2024-01-04 11:11:24','test昵称','2024-01-04 11:13:12',''),
                                                                                                                                                                                                ('主播信息查询',2001,2,'',NULL,NULL,1,0,'F','0','0','anchor:manage:info','#','test昵称','2024-01-04 11:13:01','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('账号列表查询',2007,1,'',NULL,NULL,1,0,'F','0','1','anchor:account:query','#','test昵称','2024-01-04 11:16:27','',NULL,''),
                                                                                                                                                                                                ('新增/修改直播账号',2007,2,'',NULL,NULL,1,0,'F','0','1','anchor:account:update','#','test昵称','2024-01-04 11:19:36','',NULL,''),
                                                                                                                                                                                                ('助手账号查询',2007,3,'',NULL,NULL,1,0,'F','0','1','anchor:helper:query','#','test昵称','2024-01-04 11:21:21','',NULL,''),
                                                                                                                                                                                                ('注销账号',2007,4,'',NULL,NULL,1,0,'F','0','1','anchor:account:cancel','#','test昵称','2024-01-04 11:23:27','',NULL,''),
                                                                                                                                                                                                ('封禁/解禁账号',2007,5,'',NULL,NULL,1,0,'F','0','1','anchor:account:forbidden','#','test昵称','2024-01-04 11:24:29','',NULL,''),
                                                                                                                                                                                                ('账号重置密码',2007,6,'',NULL,NULL,1,0,'F','0','1','anchor:account:resetPasswd','#','test昵称','2024-01-04 11:25:35','',NULL,''),
                                                                                                                                                                                                ('热门比赛列表查询',2008,1,'',NULL,NULL,1,0,'F','0','1','match:hot:query','#','test昵称','2024-01-04 11:31:12','',NULL,''),
                                                                                                                                                                                                ('热门比赛添加',2008,2,'',NULL,NULL,1,0,'F','0','1','match:hot:add','#','test昵称','2024-01-04 11:31:43','',NULL,''),
                                                                                                                                                                                                ('热门比赛移除',2008,3,'',NULL,NULL,1,0,'F','0','1','match:hot:remove','#','test昵称','2024-01-04 11:32:13','',NULL,''),
                                                                                                                                                                                                ('广告列表查询',2010,1,'',NULL,NULL,1,0,'F','0','1','operate:advertising:query','#','test昵称','2024-01-04 11:38:24','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('新增/修改广告',2010,2,'',NULL,NULL,1,0,'F','0','1','operate:advertising:update','#','test昵称','2024-01-04 11:39:02','',NULL,''),
                                                                                                                                                                                                ('删除广告',2010,3,'',NULL,NULL,1,0,'F','0','1','operate:advertising:delete','#','test昵称','2024-01-04 11:39:31','',NULL,''),
                                                                                                                                                                                                ('意见反馈列表查询',2011,1,'',NULL,NULL,1,0,'F','0','1','operate:feedback:query','#','test昵称','2024-01-04 11:42:23','',NULL,''),
                                                                                                                                                                                                ('回复反馈',2011,2,'',NULL,NULL,1,0,'F','0','1','operate:feedback:reply','#','test昵称','2024-01-04 11:43:00','',NULL,''),
                                                                                                                                                                                                ('忽略反馈',2011,3,'',NULL,NULL,1,0,'F','0','1','operate:feedback:ignore','#','test昵称','2024-01-04 11:43:25','',NULL,''),
                                                                                                                                                                                                ('活动列表查询',2012,1,'',NULL,NULL,1,0,'F','0','1','operate:activity:query','#','test昵称','2024-01-04 11:44:56','',NULL,''),
                                                                                                                                                                                                ('新增/修改活动',2012,2,'',NULL,NULL,1,0,'F','0','1','operate:activity:update','#','test昵称','2024-01-04 11:45:39','',NULL,''),
                                                                                                                                                                                                ('删除活动',2012,3,'',NULL,NULL,1,0,'F','0','1','operate:activity:delete','#','test昵称','2024-01-04 11:46:27','',NULL,''),
                                                                                                                                                                                                ('配置查询',2016,1,'',NULL,NULL,1,0,'F','0','1','live:heat:query','#','test昵称','2024-01-04 11:55:03','',NULL,''),
                                                                                                                                                                                                ('修改配置',2016,2,'',NULL,NULL,1,0,'F','0','1','live:heat:update','#','test昵称','2024-01-04 11:55:37','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('版本列表查询',2017,1,'',NULL,NULL,1,0,'F','0','1','app:version:query','#','test昵称','2024-01-04 11:57:34','',NULL,''),
                                                                                                                                                                                                ('新增版本',2017,2,'',NULL,NULL,1,0,'F','0','1','app:version:add','#','test昵称','2024-01-04 11:58:04','',NULL,''),
                                                                                                                                                                                                ('删除版本',2017,3,'',NULL,NULL,1,0,'F','0','1','app:version:delete','#','test昵称','2024-01-04 11:58:25','',NULL,''),
                                                                                                                                                                                                ('活动详情',2009,4,'detail','operations/detail/index',NULL,1,0,'C','1','1','','#','test昵称','2024-01-04 14:30:04','test昵称','2024-01-04 18:13:44',''),
                                                                                                                                                                                                ('用户详情',2003,2,'detail','manager/detail/index',NULL,1,0,'C','0','0',NULL,'#','test昵称','2024-01-04 18:08:25','',NULL,''),
                                                                                                                                                                                                ('用户详情',2002,2,'detail','manager/detail/index',NULL,1,0,'C','1','1','','#','test昵称','2024-01-04 18:09:00','test昵称','2024-01-04 18:09:54',''),
                                                                                                                                                                                                ('主播信息',2000,3,'details','anchor/detail/index',NULL,1,0,'C','1','1',NULL,'#','test昵称','2024-01-04 18:13:22','',NULL,''),
                                                                                                                                                                                                ('短信验证设置',2015,3,'sms','common/sms',NULL,1,0,'C','0','1',NULL,'#','test昵称','2024-03-13 15:39:31','',NULL,''),
                                                                                                                                                                                                ('短信配置查询',2052,1,'',NULL,NULL,1,0,'F','0','0','system:sms:query','#','test昵称','2024-03-15 11:02:47','',NULL,''),
                                                                                                                                                                                                ('保存短信配置',2052,2,'',NULL,NULL,1,0,'F','0','0','system:sms:add','#','test昵称','2024-03-15 11:03:13','',NULL,'');
INSERT INTO sports.sys_menu (menu_name,parent_id,order_num,`path`,component,query,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
                                                                                                                                                                                                ('短信功能配置查询',2052,4,'',NULL,NULL,1,0,'F','0','0','system:smsRule:query','#','test昵称','2024-03-15 11:04:07','',NULL,''),
                                                                                                                                                                                                ('保存功能配置',2052,4,'',NULL,NULL,1,0,'F','0','0','system:smsRule:add','#','test昵称','2024-03-15 11:04:42','',NULL,'');
-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1');
insert into sys_user_role values ('2', '2');

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '100');
insert into sys_role_dept values ('2', '101');


-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');
