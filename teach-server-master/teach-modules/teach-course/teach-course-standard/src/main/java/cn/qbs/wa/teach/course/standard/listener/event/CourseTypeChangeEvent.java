package cn.qbs.wa.teach.course.standard.listener.event;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseRelationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 课程类型变更事件
 * @author yjx
 */
@Getter
public class CourseTypeChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4430935744167734066L;

    public CourseTypeChangeEvent(CourseRelationDTO source) {
        super(source);
    }
}
