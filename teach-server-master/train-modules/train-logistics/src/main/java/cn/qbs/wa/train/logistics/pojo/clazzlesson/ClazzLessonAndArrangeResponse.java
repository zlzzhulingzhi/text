package cn.qbs.wa.train.logistics.pojo.clazzlesson;

import cn.qbs.wa.train.logistics.entity.ClazzLesson;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import lombok.Data;

import java.util.List;


@Data
public class ClazzLessonAndArrangeResponse extends ClazzLesson {

    List<ClazzLessonArrange> clazzLessonArrangeList;

}

