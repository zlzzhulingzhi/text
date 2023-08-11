package cn.qbs.wa.teach.course.standard.listener.event;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLessonChangeDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 学员课程学习统计事件
 * @author yjx
 */
@Getter
public class StudentLessonChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4430935728167734066L;

    public StudentLessonChangeEvent(CourseLessonChangeDTO params) {
        super(params);
    }
}
