package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/20 15:29
 */

@Getter
@AllArgsConstructor
public enum LiveCodeEnum {



    FL(640,368,15,800,"流畅"),

    NO(960,544,15,1000,"标清"),

    HD(1280,720,15,1500,"高清"),

    BD(1920,1080,15,2500,"超清");


    private final Integer wide;

    private final Integer height;

    private final Integer videoBitrate;

    private final Integer videoFPS;

    private final String name;
}
