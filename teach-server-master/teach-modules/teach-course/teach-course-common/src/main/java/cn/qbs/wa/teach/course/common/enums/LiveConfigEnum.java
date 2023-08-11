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
public enum LiveConfigEnum {

    RECORD("notice_mode", "通知设置"),

    LIVE("virtual_viewer_config", "虚拟观众设置"),

    MIX("client_config", "学员端设置");

    private final String code;

    private final String codeName;
}
