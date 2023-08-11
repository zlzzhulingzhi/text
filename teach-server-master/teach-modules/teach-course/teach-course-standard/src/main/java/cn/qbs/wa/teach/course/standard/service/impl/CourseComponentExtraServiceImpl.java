package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.course.standard.entity.CourseComponentExtra;
import cn.qbs.wa.teach.course.standard.mapper.CourseComponentExtraMapper;
import cn.qbs.wa.teach.course.standard.service.CourseComponentExtraService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【课程讲次内容项】(CourseComponentExtra)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2023-02-08 08:47:57
 */
@Slf4j
@Service("courseComponentExtraService")
public class CourseComponentExtraServiceImpl extends ServiceImpl<CourseComponentExtraMapper, CourseComponentExtra> implements CourseComponentExtraService {

}

