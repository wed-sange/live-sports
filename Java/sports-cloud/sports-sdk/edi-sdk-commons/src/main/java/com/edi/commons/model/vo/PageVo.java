package com.edi.commons.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 *
 */
@Data
public class PageVo<T> {
    private Integer number = 0;
    private Integer size = 10;
    @JSONField(name = "total_pages")
    private Integer totalPages = 0;
    @JSONField(name = "total_elements")
    private Long totalElements = 0L;
    private List<T> content = Collections.emptyList();

    @JSONField(name = "has_previous")
    public boolean getHasPrevious() {
        return getNumber() > 0;
    }

    @JSONField(name = "has_next")
    public boolean getHasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }

    @JSONField(name = "is_first")
    public boolean getIsFirst() {
        return !getHasPrevious();
    }

    @JSONField(name = "is_last")
    public boolean getIsLast() {
        return !getHasNext();
    }

    @JSONField(name = "has_content")
    public boolean getHasContent() {
        return !content.isEmpty();
    }
}
