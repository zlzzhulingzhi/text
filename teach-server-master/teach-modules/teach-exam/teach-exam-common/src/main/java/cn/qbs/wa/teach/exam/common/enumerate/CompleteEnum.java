package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompleteEnum {
    DURING_EXAM("0", "考试中"),
    EXAM_ENDED("1", "已完成"),
    NOT_START("4", "未开始");
    private final String index;
    private  final String name;


}
