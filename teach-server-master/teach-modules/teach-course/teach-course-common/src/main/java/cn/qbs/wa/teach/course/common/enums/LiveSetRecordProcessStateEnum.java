package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LiveSetRecordProcessStateEnum {

    ID(-1, "id"),
    TASK_ID(-2, "task_id"),
    DISABLED(0, "enabled"),
    ENABLE(1, "enabled");

    private Integer status;

    private String statusName;


}

