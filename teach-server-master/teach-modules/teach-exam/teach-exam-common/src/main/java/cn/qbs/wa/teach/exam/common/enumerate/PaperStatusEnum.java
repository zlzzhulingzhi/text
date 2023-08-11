package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaperStatusEnum {
    //0:时间未到 , 1:开始考试, 2:继续考试 , 3:重新考试 , 4:考试结束 , 5:考试次数已用完
    HAVE_NOT_STARTED("0","未开始",0),
    START_TEST("1","开始考试",1),
    CONTINUE_TO_TEST("2","继续考试",2),
    RE_EXAMINATION("3","重新考试",3),
    EXAM_ENDED("4","考试结束",4),
    NOT_TIME("5","考试次数已用完",5);

    private final String index;
    private final String name;
    private final int  flag;
}
