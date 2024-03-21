package com.edi.sdk.core;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 *
 */
@Data
public class EdiResponseQuery {
    /**
     * 返回总量
     */
    private Integer total;
    /**
     * 查询类型，id查询：sequence、time查询：time，默认sequence
     */
    private String type;
    /**
     * 查询id值，默认为0（id查询，字段返回）
     */
    private Integer id;
    /**
     * 返回数据最小id（id查询，字段返回）
     */
    @JSONField(name = "min_id")
    private Integer minId;
    /**
     * 返回数据最大id（id查询，字段返回）
     */
    @JSONField(name = "max_id")
    private Integer maxId;
    /**
     * 可返回数据最大数（id查询，字段返回）
     */
    private Integer limit;
    /**
     * 查询time值（time查询，字段返回）
     */
    private Integer time;
    /**
     * 返回数据最小time(更新时间戳)（time查询，字段返回）
     */
    @JSONField(name = "min_time")
    private Integer minTime;
    /**
     * 返回数据最大time(更新时间戳)（time查询，字段返回）
     */
    @JSONField(name = "max_time")
    private Integer maxTime;
}
