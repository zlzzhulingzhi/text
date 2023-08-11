package cn.qbs.wa.teach.course.api.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseFeeTypeEnum {

    FREE(1, "免费"),

    GOODED(2, "精品"),

    BOTH(3, "精品且免费");

    private final Integer id;

    private final String name;


}
