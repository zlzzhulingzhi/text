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
public enum LiveNoticeConfigEnum {

   STATION("station_msg", "站内信通知");


    private final String code;

    private final String codeName;
}
