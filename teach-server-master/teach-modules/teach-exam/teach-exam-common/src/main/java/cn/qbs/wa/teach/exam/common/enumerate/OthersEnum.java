package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OthersEnum {
    CALCULATED(1,"是","1"),
    NOT_CALCULATED(0,"否","0");
    private final int index;
    private final String name;
    private final String flag;
}
