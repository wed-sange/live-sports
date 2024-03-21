package org.sports.admin.manage.dao.entity.base;

import lombok.Data;

@Data
public class SlidePageBase<T> {

    private Integer size;

    private T offset;
}
