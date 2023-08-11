package cn.qbs.wa.teach.course.standard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * banner板块枚举类
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum BannerSectionEnum {
    /**
     * 课程
     */
    COURSE("course"),

    /**
     * 活动
     */
    ACTIVITY("activity"),

    ;

    private final String code;
}
