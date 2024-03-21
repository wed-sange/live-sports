package org.sports.app.service.enums;

public enum LiveStatus {
    NOT_START(0, "未开始"),
    LIVING(1, "进行中"),
    CLOSED(2, "已关闭");

    private final Integer code;
    private final String info;

    LiveStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }
    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
