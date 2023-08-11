package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LiveRecordResourceEnum {

    /**
     * 所有可见
     */
    ORIGINAL(1, "原视频"),

    /**
     * 部分可见
     */
    CLIP(2, "剪辑视频");

    private Integer type;

    private String codeName;



}

