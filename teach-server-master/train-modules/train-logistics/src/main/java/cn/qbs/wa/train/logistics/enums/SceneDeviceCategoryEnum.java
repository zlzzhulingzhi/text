package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SceneDeviceCategoryEnum {

    CLASSROOM("Classroom","教室申请"),
    DORMITORY("Dormitory","宿舍申请");

    private String code;

    private String name;


}
