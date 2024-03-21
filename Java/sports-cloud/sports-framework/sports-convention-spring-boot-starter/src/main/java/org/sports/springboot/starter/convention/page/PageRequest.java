package org.sports.springboot.starter.convention.page;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
public class PageRequest {

    /**
     * 当前页
     */
    @NotNull
    @Min(1)
    private Integer current = 1;

    /**
     * 每页显示条数
     */
    @NotNull
    @Min(1)
    private Integer size = 10;

}
