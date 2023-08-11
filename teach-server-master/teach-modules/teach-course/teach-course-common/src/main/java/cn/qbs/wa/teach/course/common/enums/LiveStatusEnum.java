package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiveStatusEnum {
    //"0未直播 1直播中 2直播结束"
    NOT("0", "未开始",0),
    LIVE("1", "直播中",1),
    END("2", "直播结束",2);

    private String code;
    private String desc;
    private Integer number;




}



