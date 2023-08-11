package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LiveShowStatusEnum {


    INIT(0, "未直播"),

    PROCESSING(1, "直播中"),

    END(2, "直播结束");


    private final Integer status;

    private final String name;



}

