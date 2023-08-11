package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuldingFloorRoomTypeEnum {

    BULDING("building"),
    FLOOR("floor"),
    CLASSROOM("classroom"),
    DORMITORY("dormitory");


    private String name;


}
