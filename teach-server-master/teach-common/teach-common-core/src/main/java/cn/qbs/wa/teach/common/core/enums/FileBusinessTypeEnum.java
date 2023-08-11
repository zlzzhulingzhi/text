package cn.qbs.wa.teach.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件业务关联类型
 *
 * @Author zcm
 * @Date 2022-06-10 13:56
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum FileBusinessTypeEnum {

    COURSE_COMPONENT(1,"课程讲次"),
    LIBRARY(2,"文库"),
    LIVE(3,"直播");

    private final Integer value;

    private final String name;

}
