package cn.qbs.wa.teach.course.standard.listener;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
import cn.qbs.wa.teach.course.common.enums.CourseTypeEnum;
import cn.qbs.wa.teach.course.standard.listener.event.CourseTypeChangeEvent;
import cn.qbs.wa.teach.course.standard.mapper.CourseComponentMapper;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseRelationDTO;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yjx
 */
@Slf4j
@Component
@AllArgsConstructor
public class CourseListener {

    private final CourseService courseService;

    private final CourseComponentMapper courseComponentMapper;

    @Async
    @Order
    @EventListener(CourseTypeChangeEvent.class)
    public void listenCourseLessonChange(CourseTypeChangeEvent event) {

        CourseRelationDTO source = (CourseRelationDTO) event.getSource();
        SecurityContextHolder.setSelectOrgId(String.valueOf(source.getOrgId()));
        SecurityContextHolder.setUserId(String.valueOf(source.getUserId()));

        Long courseId = source.getCourseId();
        if (courseId == null) {
            return;
        }
        Course course = courseService.getById(courseId);
        if (course != null) {
            String courseType = course.getCourseType();
            String changeType = course.getCourseType();
            List<String> typeList = courseComponentMapper.listTypeByCourseId(courseId);
            if (!typeList.isEmpty()) {
                if (typeList.size() > 1) {
                    changeType = CourseTypeEnum.MIX.getCode();
                } else {
                    String typeCode = typeList.get(0);
                    if (ComponentTypeEnum.ZB.getCode().equals(typeCode)) {
                        changeType = CourseTypeEnum.LIVE.getCode();
                    } else if (ComponentTypeEnum.SP.getCode().equals(typeCode)) {
                        changeType = CourseTypeEnum.RECORD.getCode();
                    } else {
                        changeType = CourseTypeEnum.MIX.getCode();
                    }
                }
            }
            if (!courseType.equals(changeType)) {
                Course update = new Course();
                update.setId(course.getId());
                update.setCourseType(changeType);
                courseService.updateById(update);
            }
        }
        SecurityContextHolder.remove();
    }

}
