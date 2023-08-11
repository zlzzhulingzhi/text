package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.standard.mapper.OrgDeskTaskCourseMapper;
import cn.qbs.wa.teach.course.standard.pojo.dto.orgdesk.OrgDeskTaskCourseInfoDTO;
import cn.qbs.wa.teach.course.standard.service.OrgDeskTaskCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【课程】(Course)表机构前台h5服务实现类
 *
 * @author WX
 * @version 1.0
 * @date 2022-03-16 14:50:37
 */
@Slf4j
@Service("orgDeskTaskCourseService")
public class OrgDeskTaskCourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements OrgDeskTaskCourseService {

    @Resource
    private OrgDeskTaskCourseMapper orgDeskTaskCourseMapper;

    @Override
    public List<OrgDeskTaskCourseInfoDTO> getCourseList(IdListAndUserIdRequest params) {
        return orgDeskTaskCourseMapper.getCourseList(params);
    }
}

