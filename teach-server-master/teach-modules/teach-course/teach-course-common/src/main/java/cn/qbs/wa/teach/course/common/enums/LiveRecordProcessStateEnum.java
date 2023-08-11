package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LiveRecordProcessStateEnum {

    ERROR(-1, "失败"),

    NORMAL(1, "正常"),

    C_TRANSCODE(2, "剪辑转码"),

    D_TRANSCODE(3, "下载转码");

    private Integer status;

    private String statusName;


}

