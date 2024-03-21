package com.edi.sdk.football.domain;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * null
 */
@Data
public class Deleted {
    /**
     * ["id - int"]没有数据，字段不存在<br/>example：[0]
     */
	@Schema(description = "[\"id - int\"]没有数据，字段不存在<br/>example：[0]",allowableValues = {"id - int"})
    private List<Object> stage;
    /**
     * ["id - int"]没有数据，字段不存在<br/>example：[0]
     */
	@Schema(description = "[\"id - int\"]没有数据，字段不存在<br/>example：[0]",allowableValues = {"id - int"})
    private List<Object> match;
    /**
     * ["id - int"]没有数据，字段不存在<br/>example：[0]
     */
	@Schema(description = "[\"id - int\"]没有数据，字段不存在<br/>example：[0]",allowableValues = {"id - int"})
    private List<Object> season;
    /**
     * ["id - int"]没有数据，字段不存在<br/>example：[0]
     */
	@Schema(description = "[\"id - int\"]没有数据，字段不存在<br/>example：[0]",allowableValues = {"id - int"})
    private List<Object> competition;
    /**
     * ["id - int"]没有数据，字段不存在<br/>example：[0]
     */
	@Schema(description = "[\"id - int\"]没有数据，字段不存在<br/>example：[0]",allowableValues = {"id - int"})
    private List<Object> team;
    /**
     * ["id - int"]没有数据，字段不存在<br/>example：[0]
     */
	@Schema(description = "[\"id - int\"]没有数据，字段不存在<br/>example：[0]",allowableValues = {"id - int"})
    private List<Object> player;
}