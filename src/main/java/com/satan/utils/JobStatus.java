package com.satan.utils;

import lombok.Getter;

public enum JobStatus {
    RUNNING(1, "ci执行中"),
    FINISH(2, "ci完成"),
    OVER_TIME(3, "ci超时"),
    CLEANED(4, "package已被清理");
    @Getter
    int type;
    @Getter
    String msg;

    JobStatus(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
