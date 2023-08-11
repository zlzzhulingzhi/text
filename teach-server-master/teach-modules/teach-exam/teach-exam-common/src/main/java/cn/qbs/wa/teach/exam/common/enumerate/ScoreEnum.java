package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScoreEnum {
    PASS(1,"考试通过"),
    NOT_PASS(2,"考试未通过");
    private final int index;
    private final String  name;
}
