package org.sports.admin.manage.service.enums;

import lombok.Getter;

/**
 * 0或者1的都适合
 */
@Getter
public enum YnEnum {
    ZERO(0),ONE(1);

    YnEnum(Integer value){
        this.value = value;
    }

    private Integer value;
}
